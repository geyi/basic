package com.airing.thread.interview;

import java.util.concurrent.TimeUnit;

public class ProducerConsumer01 {

    private static final Object[] ITEMS = new Object[10];

    private static int count, putIndex, takeIndex = 0;

    public synchronized void put(Object obj) {
        try {
            while (count == ITEMS.length) {
                this.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ITEMS[putIndex] = obj;
        if (++putIndex == ITEMS.length) {
            putIndex = 0;
        }
        count++;
        /**
         * 这里唤醒了所有等待中的线程，包括生产者线程
         * 但是此时生产者线程不应该被唤醒
         */
        this.notifyAll();
    }

    public synchronized Object take() {
        try {
            while (count == 0) {
                this.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Object item = ITEMS[takeIndex];
        if (++takeIndex == ITEMS.length) {
            takeIndex = 0;
        }
        count--;
        this.notifyAll();
        return item;
    }

    public static void main(String[] args) {
        ProducerConsumer01 producerConsumer01 = new ProducerConsumer01();

        for (int i = 0; i < 2; i++) {
            final int number = i;
            new Thread(() -> {
                String threadName = "producer" + number;
                for (int m = 0; m < 15; m++) {
                    producerConsumer01.put(new Object());
                    System.out.println(threadName + " put " + m);
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        for (int j = 0; j < 3; j++) {
            final int number = j;
            new Thread(() -> {
                String threadName = "consumer" + number;
                for (int n = 0; n < 10; n++) {
                    Object obj = producerConsumer01.take();
                    System.out.println(threadName + " take " + n);
                    try {
                        TimeUnit.MILLISECONDS.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

}
