package com.airing.pseudo.sharing;

public class CacheLinePadding02 {
    private static class Padding {
        private volatile long p1, p2, p3, p4, p5, p6, p7;
    }

    private static class T extends Padding {
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
