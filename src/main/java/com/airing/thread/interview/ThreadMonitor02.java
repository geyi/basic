package com.airing.thread.interview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ThreadMonitor02 {

    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();
        CountDownLatch latch1 = new CountDownLatch(1);
        CountDownLatch latch2 = new CountDownLatch(1);

        new Thread(() -> {
            System.out.println("t2启动");
            try {
                latch1.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("list size " + list.size() + " t2结束");
            latch2.countDown();
        }).start();

        new Thread(() -> {
            System.out.println("t1启动");
            try {
                for (int i = 0; i < 10; i++) {
                    list.add(new Object());
                    System.out.println("add " + i);
                    if (list.size() == 5) {
                        latch1.countDown();
                        latch2.await();
                    }
                }
                System.out.println("t1结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
