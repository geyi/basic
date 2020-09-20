package com.airing.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class MyNetty {
    private static final String hostname = "192.168.1.12";
    private static final int port = 9000;

    public static void main(String[] args) throws Exception {
        threadPool();
//        client();
//        nettyClient();
//        server();
//        nettyServer();
    }

    /**
     * EventLoopGroup具有线程池的功能。即，它可以接收一个可执行的任务
     *
     * @throws Exception
     */
    private static void threadPool() throws Exception {
        NioEventLoopGroup loop = new NioEventLoopGroup(1);
        loop.execute(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("hello");
            }
        });
        loop.execute(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("netty");
            }
        });
    }

    private static void client() throws Exception {
        NioEventLoopGroup loop = new NioEventLoopGroup(1);
        NioSocketChannel client = new NioSocketChannel();
        // 在netty中channel（客户端或服务端）必须注册到EventLoop中
        loop.register(client);
        ChannelFuture connectFuture = client.connect(new InetSocketAddress(hostname, port));
        // 同步等待连接成功
        ChannelFuture connect = connectFuture.sync();

        ByteBuf buffer = Unpooled.copiedBuffer("hello netty".getBytes());
//        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
//        buffer.writeBytes("hello netty".getBytes());
        ChannelFuture sendFuture = client.writeAndFlush(buffer);
        // 同步等待发送成功
        sendFuture.sync();

        // 同步等待连接关闭
        connect.channel().closeFuture().sync();

        System.out.println("client over");
    }

    private static void nettyClient() throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup loop = new NioEventLoopGroup(1);
        ChannelFuture connectFuture = bootstrap.group(loop)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new MyInHandler());
                    }
                }).connect(new InetSocketAddress(hostname, port));
        Channel clientChannel = connectFuture.sync().channel();

        ByteBuf buffer = Unpooled.copiedBuffer("hello netty".getBytes());
        ChannelFuture sendFuture = clientChannel.writeAndFlush(buffer);
        sendFuture.sync();

        clientChannel.closeFuture().sync();
    }

    private static void server() throws Exception {
        NioEventLoopGroup loop = new NioEventLoopGroup(1);
        NioServerSocketChannel server = new NioServerSocketChannel();

        ChannelPipeline pipeline = server.pipeline();
//        pipeline.addLast(new MyAcceptHandler(loop, new MyInHandler()));
        pipeline.addLast(new MyAcceptHandler(loop, new InitHandler()));

        loop.register(server);
        ChannelFuture bindFuture = server.bind(new InetSocketAddress("192.168.1.8", port));
        bindFuture.sync().channel().closeFuture().sync();
        System.out.println("server over");
    }

    private static void nettyServer() throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup loop = new NioEventLoopGroup(1);
        ChannelFuture bindFuture = bootstrap.group(loop, loop)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new MyInHandler());
                    }
                }).bind(new InetSocketAddress("192.168.1.8", port));
        bindFuture.sync().channel().closeFuture().sync();
        System.out.println("server over");
    }

    static class MyAcceptHandler extends ChannelInboundHandlerAdapter {
        private final EventLoopGroup selector;
        private final ChannelHandler handler;

        public MyAcceptHandler(EventLoopGroup thread, ChannelHandler myInHandler) {
            this.selector = thread;
            this.handler = myInHandler;
        }

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("server registerd...");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            // 服务端读到一个连接的客户端
            SocketChannel client = (SocketChannel) msg;
            ChannelPipeline p = client.pipeline();
            p.addLast(handler);
            selector.register(client);
        }
    }

    /**
     * <p>未使用@Sharable注释，当有多个客户端连接接入时，会提示如下异常：
     * <p>com.airing.netty.NettyClient$MyInHandler is not a @Sharable handler, so can't be added or removed multiple times.
     */
//    @ChannelHandler.Sharable
    static class MyInHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("client  registed...");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("client active...");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            // 客户端读到发来的数据
            ByteBuf buf = (ByteBuf) msg;
            CharSequence str = buf.getCharSequence(0, buf.readableBytes(), CharsetUtil.UTF_8);
            System.out.println(str);
            ctx.writeAndFlush(buf);
        }
    }

    /**
     * 过河拆桥
     */
    @ChannelHandler.Sharable
    static class InitHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            Channel client = ctx.channel();
            ChannelPipeline pipeline = client.pipeline();
            pipeline.addLast(new MyInHandler());
            pipeline.remove(this);
        }
    }

}
