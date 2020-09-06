package com.airing.io.reactor;

import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class SelectorThread implements Runnable {

    Selector selector;
    LinkedBlockingQueue<Channel> queue = new LinkedBlockingQueue();
    SelectorThreadGroup stg;

    public SelectorThread(SelectorThreadGroup stg) {
        try {
            selector = Selector.open();
            this.stg = stg;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                // select
                int num = selector.select();
                // processSelectedKeys
                if (num > 0) {
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            this.acceptHandler(key);
                        } else if (key.isReadable()) {
                            this.readHandler(key);
                        }
                    }
                }

                // runAllTasks
                if (!queue.isEmpty()) {
                    Channel channel = queue.take();
                    if (channel instanceof ServerSocketChannel) {
                        ServerSocketChannel server = (ServerSocketChannel) channel;
                        server.configureBlocking(false);
                        server.register(selector, SelectionKey.OP_ACCEPT);
                        System.out.println(Thread.currentThread().getName() + " server started!!!");
                    } else if (channel instanceof SocketChannel) {
                        SocketChannel client = (SocketChannel) channel;
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                        System.out.println(Thread.currentThread().getName() + " client registed read " + client.getRemoteAddress());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void acceptHandler(SelectionKey key) throws Exception {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel client = server.accept();
        client.configureBlocking(false);
        // 选择一个多路复用器注册读事件
        this.stg.nextSelector(client);
        System.out.println(Thread.currentThread().getName() + " client linked " + client.getRemoteAddress());
    }

    private void readHandler(SelectionKey key) throws Exception {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read;
        while (true) {
            read = client.read(buffer);
            if (read > 0) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    client.write(buffer);
                }
                buffer.clear();
            } else if (read == 0) {
                break;
            } else {
                client.close();
                break;
            }
        }
        System.out.println(Thread.currentThread().getName() + " client read " + client.getRemoteAddress());
    }

}
