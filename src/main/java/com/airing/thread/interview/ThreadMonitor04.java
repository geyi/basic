package com.airing.thread.interview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ThreadMonitor04 {

    static Thread t1 = null;
    static Thread t2 = null;

    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();

        Semaphore semaphore = new Semaphore(1);

        t1 = new Thread(() -> {
            System.out.println("t1启动");
            try {
                semaphore.acquire();
                for (int i = 0; i < 10; i++) {
                   list.add(new Object());
                   System.out.println("add " + i);
                   if (list.size() == 5) {
                       semaphore.release();
                       t2.join();
                   }
               }
                System.out.println("t1结束");
           } catch (Exception e) {

           } finally {

            }
        });

        t2 = new Thread(() -> {
            System.out.println("t2启动");
            try {
                semaphore.acquire();
                System.out.println("list size " + list.size() + " t2结束");
            } catch (Exception e) {

            } finally {
                semaphore.release();
            }
        });

        // 即使t1在t2之前先start，也不能保证一定是t1先获得执行权
        t1.start();
        t2.start();
//        try {
//            TimeUnit.MILLISECONDS.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

}
