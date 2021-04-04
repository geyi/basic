package com.airing.thread.juc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(5, () -> {
            System.out.println("出发！");
        });

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " 就位");
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "到咯");
            }).start();
        }
    };

}
