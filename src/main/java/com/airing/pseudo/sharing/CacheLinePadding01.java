package com.airing.pseudo.sharing;

/**
 * 缓存行对齐
 */
public class CacheLinePadding01 {
    private static class T {
        private volatile long x;
    }

    private static T[] arr = new T[2];

    static {
        arr[0] = new T();
        arr[1] = new T();
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
           for (int i = 0; i < 10_0000_0000; i++) {
               arr[0].x = i;
           }
        });
        Thread t2 = new Thread(() -> {
           for (int i = 0; i < 10_0000_0000; i++) {
               arr[1].x = i;
           }
        });
        long start = System.currentTimeMillis();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(System.currentTimeMillis() - start);
    }
}
