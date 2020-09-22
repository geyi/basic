package com.airing.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class QueueTest {
    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();
    final int limit = 5;
    final long timeout = 500;

    final Object[] items = new Object[1000];
    int putptr, takeptr, count;

    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                notFull.await();
            }
            items[putptr] = x;
            if (++putptr == items.length) {
                putptr = 0;
            }
            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public List<Object> take() throws InterruptedException {
        lock.lock();
        try {
            long future = System.currentTimeMillis() + timeout;
            long remaining = timeout;
            while (count < limit && remaining > 0) {
                notEmpty.await(remaining, TimeUnit.MILLISECONDS);
                remaining = future - System.currentTimeMillis();
            }

            int min = Math.min(count, limit);
            List<Object> list = new ArrayList<>(limit);
            for (int i = 0; i < min; i++) {
                Object item = items[takeptr];
                if (item == null) {
                    break;
                }
                list.add(item);
                if (++takeptr == items.length) {
                    takeptr = 0;
                }
                --count;
            }
            notFull.signal();
            return list;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        QueueTest queueTest = new QueueTest();

        Thread putThread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    queueTest.put(i);
                    System.out.println("put " + i);
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread putThread2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    queueTest.put(i);
                    System.out.println("put " + i);
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread takeThread = new Thread(() -> {
            try {
                int total = 0;
                for (int i = 0; i < 100; i++) {
                    List<Object> list = queueTest.take();
                    total += list.size();
                    System.out.println("take " + list.size() + ", total " + total);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        putThread.start();
        putThread2.start();
        takeThread.start();

    }
}
