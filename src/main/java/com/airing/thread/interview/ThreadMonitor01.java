package com.airing.thread.interview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import sun.rmi.runtime.Log;

public class ThreadMonitor01 {

    private static final Object lock = new Object();

    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();

        Thread t1 = null;
        Thread t2 = null;

        t2 = new Thread(() -> {
            System.out.println("t2启动");
            try {
                synchronized (lock) {
                    /**
                     * 如果没有如下if判断，直接wait
                     * 当t1运行到lock.wait时t2才启动，则t1、t2将一直处于wait状态
                     */
                    if (list.size() != 5) {

                        lock.wait();
                    }
                    System.out.println("list size " + list.size() + " t2结束");
                    lock.notify();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1 = new Thread(() -> {
            System.out.println("t1启动");
            try {
                synchronized (lock) {
                    for (int i = 0; i < 10; i++) {
                        list.add(new Object());
                        System.out.println("add " + i);
                        if (list.size() == 5) {
                            lock.notify();
                            lock.wait();
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
//        try {
//            TimeUnit.SECONDS.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        t2.start();
    }

}
