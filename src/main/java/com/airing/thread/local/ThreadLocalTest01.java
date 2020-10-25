package com.airing.thread.local;

import java.util.concurrent.TimeUnit;

public class ThreadLocalTest01 {

    static String name = "zhangsan";
//    static int name = 0;
//    public static boolean name = true;

    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            try {
                /*System.out.println(name);
                TimeUnit.SECONDS.sleep(2);
                System.out.println(name);*/
                System.out.println("running");
                while ("zhangsan".equals(name)) {
                }
                System.out.println("stop");
            } catch (Exception e) {

            }
        }).start();

        /*new Thread(() -> {
            try {

                TimeUnit.SECONDS.sleep(1);
                name = 2;
            } catch (Exception e) {

            }
        }).start();*/

        TimeUnit.SECONDS.sleep(1);
        name = "lisi";

    }

}
