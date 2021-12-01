package com.airing.queue;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class QueueGroup<T> {
    TimeoutBlockingQueue<T>[] queues;
    AtomicInteger ai = new AtomicInteger(0);
    int queueNum;

    public QueueGroup(int queueNum, T[] items) {
        this.queueNum = queueNum;
        TimeoutBlockingQueue<T>[] queues = new TimeoutBlockingQueue[queueNum];
        for (int i = 0; i < queueNum; i++) {
            queues[i] = new TimeoutBlockingQueue<>(items);
            new Thread(new Consumer<>(queues[i])).start();
        }
    }

    private static class Consumer<T> implements Runnable {
        private final TimeoutBlockingQueue<T> queue;

        public Consumer(TimeoutBlockingQueue<T> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                List<T> list = null;
                try {
                    list = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (list == null || list.isEmpty()) {
                    continue;
                }
                System.out.println(list);
            }
        }
    }

    public void put(T obj) throws InterruptedException {
        int idx = ai.getAndIncrement() % queueNum;
        queues[idx].put(obj);
    }
}
