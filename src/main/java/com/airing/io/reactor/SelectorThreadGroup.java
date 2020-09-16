package com.airing.io.reactor;

import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class SelectorThreadGroup {

    SelectorThread[] selectors;
    ServerSocketChannel server;
    AtomicInteger ai = new AtomicInteger(0);
    int threadNum;

    public SelectorThreadGroup(int threadNum) {
        if (threadNum <= 0) {
            throw new RuntimeException("threadNum can not lte 0");
        }
        this.threadNum = threadNum;
        selectors = new SelectorThread[threadNum];
        for (int i = 0; i < threadNum; i++) {
            selectors[i] = new SelectorThread(this);
            new Thread(selectors[i]).start();
        }
    }

    public void bind(int port) {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));
            this.nextSelector(server);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SelectorThread next() {
        int curNum = this.ai.getAndIncrement();
        int index = curNum % this.threadNum;
        return selectors[index];
    }

    public void nextSelector(Channel channel) throws Exception {
        SelectorThread st = this.next();
        /*
        为什么不在这里调用register方法注册channel？
        因为如果SelectorThread中的多路复用器在执行以下代码之前阻塞在select方法上
        那么这里的register方法就会被阻塞，导致selector永远不会被唤醒
         */
        /*
        ServerSocketChannel server = (ServerSocketChannel) channel;
        server.register(st.selector, SelectionKey.OP_ACCEPT);
        st.selector.wakeup();
        */
        st.queue.put(channel);
        st.selector.wakeup();
    }

}
