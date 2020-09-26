package com.airing.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class ProxyFactory {

    public static <T> T getInstance(Class<T> interfaceInfo) {
        ClassLoader classLoader = interfaceInfo.getClassLoader();
        Class<?>[] interfaces = {interfaceInfo};
        return (T) Proxy.newProxyInstance(classLoader, interfaces, (proxy, method, args) -> {

            // 调用的服务，方法，参数 -> 封装成message

            String serviceName = interfaceInfo.getName();
            String methodName = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();

            Body body = new Body();
            body.setServiceName(serviceName);
            body.setMethodName(methodName);
            body.setParameterTypes(parameterTypes);
            body.setArgs(args);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(body);
            byte[] msgBody = out.toByteArray();

            // requestId + message，本地要缓存requestId

            Header header = new Header();
            header.setCode(0xABEF0819);
            header.setRequestId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
            header.setBodyLength(msgBody.length);

            out.reset();
            objOut = new ObjectOutputStream(out);
            objOut.writeObject(header);
            byte[] msgHeader = out.toByteArray();

            System.out.println(msgHeader.length);

            CountDownLatch countDownLatch = new CountDownLatch(1);

            CallbackHandler.add(header.getRequestId(), () -> {
                    countDownLatch.countDown();
            });

            // 连接池
            NioSocketChannel client = ClientFactory.getInstance()
                    .getClient(new InetSocketAddress("127.0.0.1", 9000));

            // 发送
            ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer((msgHeader.length + msgBody.length));
            byteBuf.writeBytes(msgHeader);
            byteBuf.writeBytes(msgBody);
            ChannelFuture sendFuture = client.writeAndFlush(byteBuf);
            // sync仅代表等待发送完成。响应什么时候回来并不知道。
            sendFuture.sync();

            countDownLatch.await();

            // 未来返回，回调



            return null;
        });
    }

    public static void main(String[] args) {
        RpcService rpcService = ProxyFactory.getInstance(RpcService.class);
        rpcService.rpcMethod("hello");
    }

}
