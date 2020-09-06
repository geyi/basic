package com.airing.pseudo.sharing;

import sun.misc.Contended;

public class CacheLinePaddingContended {
    @Contended
    volatile long l1;
    @Contended
    volatile long l2;

    public static void main(String[] args) throws InterruptedException {
        CacheLinePaddingContended clp = new CacheLinePaddingContended();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10_0000_0000; i++) {
                clp.l1 = i;
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10_0000_0000; i++) {
                clp.l2 = i;
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
