package com.airing.thread.juc;

import java.util.concurrent.TimeUnit;

public class VolatileTest {

    public static /*volatile*/ boolean runnable = true;

    public static void m() {
        try {
            System.out.println("m started!");
            while (runnable) {
                /* 存在这段代码时，不加volatile线程同样可以停止运行！？
                System.out.println("running");
                TimeUnit.MILLISECONDS.sleep(100);*/
            }
            System.out.println("m ended!");
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) throws Exception {
        new Thread(VolatileTest::m).start();
        TimeUnit.SECONDS.sleep(1);
        runnable = false;
    }

}
