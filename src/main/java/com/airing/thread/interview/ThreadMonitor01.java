package com.airing.thread.interview;

import java.util.ArrayList;
import java.util.List;

public class ThreadMonitor01 {

    private static final Object lock = new Object();

    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();

        new Thread(() -> {
            System.out.println("t2启动");
            synchronized (lock) {
                if (list.size() < 5) {
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("list size " + list.size());
                lock.notify();
            }
        }).start();

        new Thread(() -> {
            System.out.println("t1启动");
            synchronized (lock) {
                for (int i = 0; i < 10; i++) {
                    list.add(new Object());
                    System.out.println("add " + i);
                    if (list.size() == 5) {
                        lock.notify();
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

}
