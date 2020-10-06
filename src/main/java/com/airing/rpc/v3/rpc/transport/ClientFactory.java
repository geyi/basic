package com.airing.rpc.v3.rpc.transport;

import com.airing.rpc.RpcConstant;
import com.airing.rpc.v3.rpc.CallbackHandler;
import com.airing.rpc.v3.rpc.protocol.Body;
import com.airing.rpc.v3.rpc.protocol.Header;
import com.airing.rpc.v3.rpc.protocol.Package;
import com.airing.rpc.v3.util.SerialUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ClientFactory {
    private volatile static ClientFactory clientFactory;
    private ConcurrentHashMap<InetSocketAddress, ClientPool> outBoxs = new ConcurrentHashMap<>();
    private final int poolsize = 8;
    private final Random random = new Random();

    private ClientFactory() {
    }

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

    public CompletableFuture<Object> transport(Body body) {
        byte[] msgBody = SerialUtil.objToByte(body);

        Header header = new Header();
        header.setCode(0xABEF0819);
        header.setRequestId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
        header.setBodyLength(msgBody.length);

        byte[] msgHeader = SerialUtil.objToByte(header);

        System.out.println(msgHeader.length);

        CompletableFuture<Object> completableFuture = new CompletableFuture();
        CallbackHandler.add(header.getRequestId(), completableFuture);

        // 连接池
        NioSocketChannel client = ClientFactory.getInstance()
                .getClient(new InetSocketAddress("127.0.0.1", 9000));

        // 发送
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer((msgHeader.length + msgBody.length));
        byteBuf.writeBytes(msgHeader);
        byteBuf.writeBytes(msgBody);
        client.writeAndFlush(byteBuf);

        return completableFuture;
    }

    public NioSocketChannel getClient(InetSocketAddress address) {
        ClientPool clientPool = outBoxs.get(address);
        if (clientPool == null) {
            synchronized (outBoxs) {
                if (clientPool == null) {
                    outBoxs.putIfAbsent(address, new ClientPool(poolsize));
                    clientPool = outBoxs.get(address);
                }
            }
        }

        int index = random.nextInt(poolsize);
        if (clientPool.clients[index] != null && clientPool.clients[index].isActive()) {
            return clientPool.clients[index];
        } else {
            synchronized (clientPool.locks[index]) {
                if (clientPool.clients[index] == null || !clientPool.clients[index].isActive()) {
                    clientPool.clients[index] = createClient(address);
                }
            }
        }

        return clientPool.clients[index];
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
                        pipeline.addLast(new ResponseDecoder());
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

    static class ResponseDecoder extends ByteToMessageDecoder {
        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
            while (byteBuf.readableBytes() >= RpcConstant.PACKAGE_LENGTH) {
                byte[] headerByte = new byte[RpcConstant.PACKAGE_LENGTH];
                byteBuf.getBytes(byteBuf.readerIndex(), headerByte);
                ByteArrayInputStream headerInput = new ByteArrayInputStream(headerByte);
                ObjectInputStream headerObjInput = new ObjectInputStream(headerInput);
                Header header = (Header) headerObjInput.readObject();

                if (byteBuf.readableBytes() >= header.getBodyLength()) {
                    byte[] bodyByte = new byte[(int) header.getBodyLength()];
                    byteBuf.readBytes(RpcConstant.PACKAGE_LENGTH);
                    byteBuf.readBytes(bodyByte);
                    ByteArrayInputStream bodyInput = new ByteArrayInputStream(bodyByte);
                    ObjectInputStream bodyObjInput = new ObjectInputStream(bodyInput);
                    Body body = (Body) bodyObjInput.readObject();
                    list.add(new Package(header, body));
                } else {
                    break;
                }
            }
        }
    }

    class ResponseHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Package pkg = (Package) msg;
            CallbackHandler.run(pkg);
        }
    }

}
