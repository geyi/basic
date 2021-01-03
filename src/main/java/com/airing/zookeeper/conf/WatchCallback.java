package com.airing.zookeeper.conf;

import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class WatchCallback implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {

    private MyConf myConf;
    private ZooKeeper zk;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public MyConf getMyConf() {
        return myConf;
    }

    public void setMyConf(MyConf myConf) {
        this.myConf = myConf;
    }

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public void await() throws InterruptedException {
        zk.exists("/appConf", this, this, "BASIC");
        countDownLatch.await();
    }

    @Override
    public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
        if (stat != null) {
            String conf = new String(bytes);
            myConf.setConf(conf);
            countDownLatch.countDown();
        }

    }

    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        if (stat != null) {
            zk.getData("/appConf", this, this, "BASIC");
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        Event.EventType type = watchedEvent.getType();
        switch (type) {
            case None:
                break;
            case NodeCreated:
                zk.getData("/appConf", this, this, "BASIC");
                break;
            case NodeDeleted:
                break;
            case NodeDataChanged:
                zk.getData("/appConf", this, this, "BASIC");
                break;
            case NodeChildrenChanged:
                break;
        }
    }
}
