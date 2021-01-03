package com.airing.zookeeper.conf;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.ZooKeeper;

public class ZKUtils {

    private volatile static ZooKeeper zk;
    private static final String ADDRESS = "172.16.25.133:2181/testConf";
    private static final Object lock = new Object();
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static ZooKeeper getZk() {
        if (zk == null) {
            synchronized (lock) {
                if (zk == null) {
                    try {
                        zk = new ZooKeeper(ADDRESS, 3000, new DefaultWatcher(countDownLatch));
                        countDownLatch.await();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return zk;
    }

}
