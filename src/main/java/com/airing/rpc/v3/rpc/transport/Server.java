package com.airing.rpc.v3.rpc.transport;

import com.airing.rpc.RpcConstant;
import com.airing.rpc.v3.rpc.Dispatcher;
import com.airing.rpc.v3.rpc.protocol.Body;
import com.airing.rpc.v3.rpc.protocol.Header;
import com.airing.rpc.v3.rpc.protocol.Package;
import com.airing.rpc.v3.service.RpcService;
import com.airing.rpc.v3.service.impl.RpcServiceImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;

public class Server {

    public static void main(String[] args) throws Exception {
        RpcServiceImpl rpcService = new RpcServiceImpl();
        Dispatcher.getInstance().regist(RpcService.class.getName(), rpcService);
        startup();
    }

    public static void startup() throws Exception {
        NioEventLoopGroup boss = new NioEventLoopGroup(8);
        NioEventLoopGroup worder = boss;
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture bindFuture = serverBootstrap.group(boss, worder)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        ChannelPipeline pipeline = nioSocketChannel.pipeline();
                        pipeline.addLast(new RequestDecoder());
                        pipeline.addLast(new RequestHandler());
                    }
                }).bind(new InetSocketAddress("127.0.0.1", 9000));

        Channel server = bindFuture.sync().channel();
        System.out.println("server startup!!!");
        server.closeFuture().sync();
    }

    static class RequestDecoder extends ByteToMessageDecoder {
        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
//            System.out.println("bytebuf size " + byteBuf.readableBytes());
            while (byteBuf.readableBytes() >= RpcConstant.PACKAGE_LENGTH) {
                byte[] headerByte = new byte[RpcConstant.PACKAGE_LENGTH];
//                data.readBytes(headerByte);
                // 不会移动指针
                byteBuf.getBytes(byteBuf.readerIndex(), headerByte);
                ByteArrayInputStream headerInput = new ByteArrayInputStream(headerByte);
                ObjectInputStream headerObjInput = new ObjectInputStream(headerInput);
                Header header = (Header) headerObjInput.readObject();
                System.out.println("server receive: " + header.getRequestId()
                        + ", content length: " + header.getBodyLength()
                        + ", readable: " + byteBuf.readableBytes());

                if (byteBuf.readableBytes() - RpcConstant.PACKAGE_LENGTH >= header.getBodyLength()) {
                    byte[] bodyByte = new byte[(int) header.getBodyLength()];
                    // 移动指针到body开始的位置
                    byteBuf.readBytes(RpcConstant.PACKAGE_LENGTH);
//                    data.readBytes(bodyByte);
                    byteBuf.readBytes(bodyByte);
                    ByteArrayInputStream bodyInput = new ByteArrayInputStream(bodyByte);
                    ObjectInputStream bodyObjInput = new ObjectInputStream(bodyInput);
                    Body body = (Body) bodyObjInput.readObject();
//                    System.out.println(body.getServiceName());
//                    System.out.println(body.getMethodName());
                    list.add(new Package(header, body));
                } else {
                    System.out.println("read body else: " + byteBuf.readableBytes());
                    break;
                }
            }
            System.out.println("read body while: " + byteBuf.readableBytes());
        }
    }

    static class RequestHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Package pkg = (Package) msg;
            String ioThreadName = Thread.currentThread().getName();
            ctx.executor().execute(() -> {
                String execThreadName = Thread.currentThread().getName();

                String s = pkg.getHeader().getRequestId() + ", args: " + pkg.getBody().getArgs()[0]
                        + ", ioThreadName: " + ioThreadName
                        + ", execThreadName: " + execThreadName;
                System.out.println(s);

                String serviceName = pkg.getBody().getServiceName();
                String methodName = pkg.getBody().getMethodName();
                Object service = Dispatcher.getInstance().get(serviceName);
                Class<?> clazz = service.getClass();

                try {
                    Method method = clazz.getMethod(methodName, pkg.getBody().getParameterTypes());
                    Object ret = method.invoke(service, pkg.getBody().getArgs());

                    Body body = new Body();
                    body.setResponse(String.valueOf(ret));

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    ObjectOutputStream objOut = new ObjectOutputStream(out);
                    objOut.writeObject(body);
                    byte[] msgBody = out.toByteArray();

                    // requestId + message，本地要缓存requestId

                    Header header = new Header();
                    header.setCode(0xABEF0821);
                    header.setRequestId(pkg.getHeader().getRequestId());
                    header.setBodyLength(msgBody.length);

                    out.reset();
                    objOut = new ObjectOutputStream(out);
                    objOut.writeObject(header);
                    byte[] msgHeader = out.toByteArray();

                    ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer((msgHeader.length + msgBody.length));
                    byteBuf.writeBytes(msgHeader);
                    byteBuf.writeBytes(msgBody);
                    ctx.writeAndFlush(byteBuf);
                } catch (Exception e) {

                }

            });


        }
    }

}
