package com.airing.zookeeper.conf;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.apache.zookeeper.ZooKeeper;

public class Test {
    public static void main(String[] args) throws Exception {
        ZooKeeper zk = ZKUtils.getZk();
        MyConf myConf = new MyConf();
        WatchCallback watchCallback = new WatchCallback();
        watchCallback.setZk(zk);
        watchCallback.setMyConf(myConf);
        watchCallback.await();

        while (true) {
            System.out.println(myConf.getConf());
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
