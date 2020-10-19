package com.airing.thread.interview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

public class ThreadMonitor03 {

    static Thread t1 = null;
    static Thread t2 = null;

    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();

        t2 = new Thread(() -> {
            System.out.println("t2启动");
            if (list.size() != 5) {
                LockSupport.park();
            }
            System.out.println("list size " + list.size());
            LockSupport.unpark(t1);
        });

        t1 = new Thread(() -> {
            System.out.println("t1启动");
            for (int i = 0; i < 10; i++) {
                list.add(new Object());
                System.out.println("add " + i);
                if (list.size() == 5) {
                    LockSupport.unpark(t2);
                    LockSupport.park();
                }
            }
        });

        t1.start();
        t2.start();
    }

}
