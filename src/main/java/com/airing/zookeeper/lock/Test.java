package com.airing.zookeeper.lock;

import java.util.concurrent.TimeUnit;
import org.apache.zookeeper.ZooKeeper;

public class Test {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                ZooKeeper zk = ZKUtils.getZk();
                WatchCallback watchCallback = new WatchCallback();
                watchCallback.setZk(zk);
                watchCallback.setThreadName(Thread.currentThread().getName());
                // try lock
                watchCallback.tryLock();
                // do something
                System.out.println(Thread.currentThread().getName() + " working");
                /*try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                // release lock
                watchCallback.release();
            }).start();
        }
    }

}
