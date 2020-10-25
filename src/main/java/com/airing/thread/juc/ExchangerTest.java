package com.airing.thread.juc;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class ExchangerTest {

    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                String localValue = Thread.currentThread().getName();
                String value = exchanger.exchange(localValue);
                System.out.println(localValue + " " + value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t0").start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                String localValue = Thread.currentThread().getName();
                String value = exchanger.exchange(localValue);
                System.out.println(localValue + " " + value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1").start();
    }

}
