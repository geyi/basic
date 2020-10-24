package com.airing.thread.interview;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer02 {

    private static Lock lock = new ReentrantLock();
    private static Condition producerCondition = lock.newCondition();
    private static Condition consumerCondition = lock.newCondition();
    private static final Object[] ITEMS = new Object[10];

    private static int count, putIndex, takeIndex = 0;

    public static void put(Object obj) {
        lock.lock();
        try {
            while (count == ITEMS.length) {
                producerCondition.await();
            }

            ITEMS[putIndex] = obj;
            if (++putIndex == ITEMS.length) {
                putIndex = 0;
            }
            count++;
            consumerCondition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static Object take() {
        lock.lock();
        try {
            while (count == 0) {
                consumerCondition.await();
            }

            Object item = ITEMS[takeIndex];
            if (++takeIndex == ITEMS.length) {
                takeIndex = 0;
            }
            count--;
            producerCondition.signalAll();
            return item;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        int producerCount = 1;
        int consumerCount = 1;

        for (int i = 0; i < producerCount; i++) {
            final int number = i;
            new Thread(() -> {
                String threadName = "producer" + number;
                for (int m = 0; m < 10; m++) {
                    ProducerConsumer02.put(new Object());
                    System.out.println(threadName + " put " + m);
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        for (int j = 0; j < consumerCount; j++) {
            final int number = j;
            new Thread(() -> {
                String threadName = "consumer" + number;
                for (int n = 0; n < 10; n++) {
                    Object obj = ProducerConsumer02.take();
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
