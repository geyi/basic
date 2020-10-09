package com.airing;

import java.util.concurrent.TimeUnit;

public class Test {

    private static final Object lock = new Object();

    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(() -> {
            synchronized (lock) {
                try {
                    for (int i = 0; i < 10; i++) {
                        if (i == 0) {
                            // 运行中的线程是RUNNABLE状态
                            System.out.println("当前线程的状态：" + Thread.currentThread().getState());
                        }
                        System.out.println(Thread.currentThread().getName() + "-" + i);
                        TimeUnit.MILLISECONDS.sleep(100);
                        if (i == 5) {
                            lock.wait();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (lock) {
                try {
                    for (int i = 0; i < 10; i++) {
                        System.out.println(Thread.currentThread().getName() + "-" + i);
                        TimeUnit.MILLISECONDS.sleep(100);
                    }
                    lock.notify();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 新创建的Thread对象，在调用start方法前，线程处于NEW状态
        System.out.println(thread.getName() + " state " + thread.getState());
        System.out.println(thread2.getName() + " state " + thread2.getState());

        thread.start();
        // 调用start方法后线程进入RUNNABLE状态
        System.out.println(thread.getName() + " state " + thread.getState());
        TimeUnit.MILLISECONDS.sleep(10);
        thread2.start();
        // 调用start方法后线程进入RUNNABLE状态
        System.out.println(thread2.getName() + " state " + thread2.getState());

        TimeUnit.MILLISECONDS.sleep(300);
        // 当线程调用了sleep(timeout)方法时，线程处于TIMED_WAITING状态
        System.out.println(thread.getName() + " state " + thread.getState());
        // 当线程未获得锁时处于BLOCKED状态
        System.out.println(thread2.getName() + " state " + thread2.getState());

        TimeUnit.MILLISECONDS.sleep(500);
        // 当线程调用了wait()方法时，线程处于WAITING状态
        System.out.println(thread.getName() + " state " + thread.getState());
        // 当线程调用了sleep(timeout)方法时，线程处于TIMED_WAITING状态
        System.out.println(thread2.getName() + " state " + thread2.getState());

        thread.join();
        thread2.join();
        // 当线程运行结束后处于TERMINATED状态
        System.out.println(thread.getName() + " state " + thread.getState());
        System.out.println(thread2.getName() + " state " + thread2.getState());

        /**
         * Thread-0 state NEW
         * Thread-1 state NEW
         * Thread-0 state RUNNABLE
         * 当前线程的状态：RUNNABLE
         * Thread-0-0
         * Thread-1 state RUNNABLE
         * Thread-0-1
         * Thread-0-2
         * Thread-0-3
         * Thread-0 state TIMED_WAITING
         * Thread-1 state BLOCKED
         * Thread-0-4
         * Thread-0-5
         * Thread-1-0
         * Thread-1-1
         * Thread-1-2
         * Thread-0 state WAITING
         * Thread-1 state TIMED_WAITING
         * Thread-1-3
         * Thread-1-4
         * Thread-1-5
         * Thread-1-6
         * Thread-1-7
         * Thread-1-8
         * Thread-1-9
         * Thread-0-6
         * Thread-0-7
         * Thread-0-8
         * Thread-0-9
         * Thread-0 state TERMINATED
         * Thread-1 state TERMINATED
         */
    }

}
