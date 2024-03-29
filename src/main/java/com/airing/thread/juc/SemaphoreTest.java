package com.airing.thread.juc;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreTest {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1);

        new Thread(() -> {
            try {
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName() + " running");
                TimeUnit.SECONDS.sleep(1);
                System.out.println(Thread.currentThread().getName() + " done");
            } catch (Exception e) {

            } finally {
                semaphore.release();
            }
        }).start();

        new Thread(() -> {
            try {
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName() + " running");
                TimeUnit.SECONDS.sleep(1);
                System.out.println(Thread.currentThread().getName() + " done");
            } catch (Exception e) {

            } finally {
                semaphore.release();
            }
        }).start();
    }

}
