package com.airing.io.reactor;

import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class SelectorThreadGroup {

    SelectorThread[] selectors;
    ServerSocketChannel server;
    AtomicInteger ai = new AtomicInteger(0);
    int threadNum;

    public SelectorThreadGroup(int threadNum) {
        if (threadNum <= 0) {
            throw new RuntimeException("threadNum is not lte 0");
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
        st.queue.put(channel);
        st.selector.wakeup();
    }

}
