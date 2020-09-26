package com.airing.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ClientFactory {
    private volatile static ClientFactory clientFactory;
    private ConcurrentHashMap<InetSocketAddress, ClientPool> outBoxs = new ConcurrentHashMap<>();
    private final int poolsize = 1;
    private final Random random = new Random();

    private ClientFactory() {}

    public static ClientFactory getInstance() {
        if (clientFactory == null) {
            synchronized (ClientFactory.class) {
                if (clientFactory == null) {
                    clientFactory = new ClientFactory();
                }
            }
        }
        return clientFactory;
    }

    public NioSocketChannel getClient(InetSocketAddress address) {
        ClientPool clientPool = outBoxs.get(address);
        if (clientPool == null) {
            outBoxs.putIfAbsent(address, new ClientPool(poolsize));
            clientPool = outBoxs.get(address);
        }

        int index = random.nextInt(poolsize);
        if (clientPool.clients[index] != null && clientPool.clients[index].isActive()) {
            return clientPool.clients[index];
        }

        synchronized (clientPool.locks[index]) {
            return clientPool.clients[index] = createClient(address);
        }
    }

    private NioSocketChannel createClient(InetSocketAddress address) {
        NioEventLoopGroup selector = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture connectFuture = bootstrap.group(selector)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        ChannelPipeline pipeline = nioSocketChannel.pipeline();
                        pipeline.addLast(new ResponseHandler());
                    }
                }).connect(address);
        try {
            return (NioSocketChannel) connectFuture.sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    class ResponseHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf data = (ByteBuf) msg;
            if (data.readableBytes() >= 94) {
                byte[] headerByte = new byte[94];
                data.readBytes(headerByte);
                ByteArrayInputStream headerInput = new ByteArrayInputStream(headerByte);
                ObjectInputStream headerObjInput = new ObjectInputStream(headerInput);
                Header header = (Header) headerObjInput.readObject();
                System.out.println("client send: " + header.getRequestId() + ", content length: " + header.getBodyLength());

                CallbackHandler.run(header.getRequestId());

                /*if (data.readableBytes() >= header.getBodyLength()) {
                    byte[] bodyByte = new byte[(int) header.getBodyLength()];
                    data.readBytes(bodyByte);
                    ByteArrayInputStream bodyInput = new ByteArrayInputStream(bodyByte);
                    ObjectInputStream bodyObjInput = new ObjectInputStream(bodyInput);
                    Body body = (Body) bodyObjInput.readObject();
                    System.out.println(body.getServiceName());
                    System.out.println(body.getMethodName());
                }*/
            }
        }
    }

}
