package com.airing.rpc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;

public class Server {

    public static void main(String[] args) throws Exception {
        startup();
    }

    public static void startup() throws Exception {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worder = boss;
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture bindFuture = serverBootstrap.group(boss, worder)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel serverSocketChannel) throws Exception {
                        ChannelPipeline pipeline = serverSocketChannel.pipeline();
                        pipeline.addLast(new RequestHandler());
                    }
                }).bind(new InetSocketAddress("127.0.0.1", 9000));

        Channel server = bindFuture.sync().channel();
        server.closeFuture().sync();


    }

    static class RequestHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf data = (ByteBuf) msg;
            ByteBuf sendBuf = data.copy();
            if (data.readableBytes() >= 94) {
                byte[] headerByte = new byte[94];
                data.readBytes(headerByte);
                ByteArrayInputStream headerInput = new ByteArrayInputStream(headerByte);
                ObjectInputStream headerObjInput = new ObjectInputStream(headerInput);
                Header header = (Header) headerObjInput.readObject();
                System.out.println("server receive: " + header.getRequestId() + ", content length: " + header.getBodyLength());

                if (data.readableBytes() >= header.getBodyLength()) {
                    byte[] bodyByte = new byte[(int) header.getBodyLength()];
                    data.readBytes(bodyByte);
                    ByteArrayInputStream bodyInput = new ByteArrayInputStream(bodyByte);
                    ObjectInputStream bodyObjInput = new ObjectInputStream(bodyInput);
                    Body body = (Body) bodyObjInput.readObject();
                    System.out.println(body.getServiceName());
                    System.out.println(body.getMethodName());
                }
            }

            ctx.writeAndFlush(sendBuf);
        }
    }

}
