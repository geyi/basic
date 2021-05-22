package com.airing.io.socket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

public class SocketNIO {

    public static void main(String[] args) throws Exception {

        LinkedList<SocketChannel> clients = new LinkedList<>();

        ServerSocketChannel server = ServerSocketChannel.open(); // 服务端开启监听：接受客户端
        server.bind(new InetSocketAddress(9001));
        server.configureBlocking(false); // 重点 OS NONBLOCKING!!!

//        server.setOption(StandardSocketOptions.TCP_NODELAY, false);
//        StandardSocketOptions.TCP_NODELAY
//        StandardSocketOptions.SO_KEEPALIVE
//        StandardSocketOptions.SO_LINGER
//        StandardSocketOptions.SO_RCVBUF
//        StandardSocketOptions.SO_SNDBUF
//        StandardSocketOptions.SO_REUSEADDR


        while (true) {
            // 接受客户端的连接
            Thread.sleep(1000);
            SocketChannel client = server.accept(); // 不会阻塞

            if (client == null) {
                System.out.println("null.....");
            } else {
                client.configureBlocking(false);
                int port = client.socket().getPort();
                System.out.println("client port: " + port);
                clients.add(client);
            }

            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            // 遍历已经链接进来的客户端能不能读写数据
            for (SocketChannel c : clients) {
                int num = c.read(buffer); // 不会阻塞，返回 >0  -1  0
                if (num > 0) {
                    // 从写模式切换成读模式，将position设置为0，limit设置为之前position的值
                    buffer.flip();
                    byte[] data = new byte[buffer.limit()];
                    buffer.get(data);

                    String s = new String(data);
                    System.out.println(c.socket().getPort() + ": " + s);
                    // 将position设置为0，limit设置为capacity的值
                    buffer.clear();
                }
            }
        }
    }

}
