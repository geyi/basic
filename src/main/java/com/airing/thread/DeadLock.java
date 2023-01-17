package com.airing.thread;

import java.util.concurrent.TimeUnit;

public class DeadLock {
    public static void main(String[] args) {
        final Object lock1 = new Object();
        final Object lock2 = new Object();

        new Thread(() -> {
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName() + " get lock1");
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2) {
                    System.out.println(Thread.currentThread().getName() + " get lock2");
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName() + " get lock2");
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock1) {
                    System.out.println(Thread.currentThread().getName() + " get lock1");
                }
            }
        }).start();
    }
}
