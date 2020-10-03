package com.airing.rpc.v2;

import com.airing.rpc.RpcConstant;
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
import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;

/**
 * v2版本解决了粘包拆包的问题
 */
public class Server {

    public static void main(String[] args) throws Exception {
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
            while (byteBuf.readableBytes() >= RpcConstant.PACKAGE_LENGTH) {
                byte[] headerByte = new byte[RpcConstant.PACKAGE_LENGTH];
//                data.readBytes(headerByte);
                byteBuf.getBytes(byteBuf.readerIndex(), headerByte);
                ByteArrayInputStream headerInput = new ByteArrayInputStream(headerByte);
                ObjectInputStream headerObjInput = new ObjectInputStream(headerInput);
                Header header = (Header) headerObjInput.readObject();
                System.out.println("server receive: " + header.getRequestId() + ", content length: " + header.getBodyLength());

                if (byteBuf.readableBytes() >= header.getBodyLength()) {
                    byte[] bodyByte = new byte[(int) header.getBodyLength()];
                    byteBuf.readBytes(RpcConstant.PACKAGE_LENGTH);
//                    data.readBytes(bodyByte);
                    byteBuf.readBytes(bodyByte);
                    ByteArrayInputStream bodyInput = new ByteArrayInputStream(bodyByte);
                    ObjectInputStream bodyObjInput = new ObjectInputStream(bodyInput);
                    Body body = (Body) bodyObjInput.readObject();
                    System.out.println(body.getServiceName());
                    System.out.println(body.getMethodName());
                    list.add(new Package(header, body));
                } else {
                    System.out.println("read body else: " + byteBuf.readableBytes());
                    break;
                }
            }
        }
    }

    static class RequestHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            /* 具有粘包拆包的问题的代码，当一个数据包不是以header开始时就会报错
            ByteBuf data = (ByteBuf) msg;
            ByteBuf sendBuf = data.copy();
            System.out.println("channel start: " + data.readableBytes());
            while (data.readableBytes() >= RpcConstant.PACKAGE_LENGTH) {
                byte[] headerByte = new byte[RpcConstant.PACKAGE_LENGTH];
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
                } else {
                    System.out.println("read body else: " + data.readableBytes());
                }
            } *//* else {
                System.out.println("read header else: " + data.readableBytes());
            }*/


            /*
            直接在当前方法中处理I/O、业务和数据的返回
            创建自定义的线程池处理业务及返回
            使用netty的EventLoop来处理业务及返回
             */


            Package pkg = (Package) msg;
            String ioThreadName = Thread.currentThread().getName();
            ctx.executor().execute(() -> {
                String execThreadName = Thread.currentThread().getName();

                String s = pkg.getHeader().getRequestId() + ", args: " + pkg.getBody().getArgs()[0]
                        + ", ioThreadName: " + ioThreadName
                        + ", execThreadName: " + execThreadName;
                System.out.println(s);
                pkg.getBody().setResponse(s);

                try {
                    Body body = new Body();
                    body.setResponse(s);

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
