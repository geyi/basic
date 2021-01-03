package com.airing.zookeeper.lock;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class WatchCallback implements Watcher, AsyncCallback.StringCallback, AsyncCallback.Children2Callback, AsyncCallback.StatCallback {

    private ZooKeeper zk;
    private String threadName;
    private String pathName;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public void tryLock() {
        zk.create("/lock", threadName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,
                this,
                "BASIC");
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        try {
            zk.delete(pathName, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        Event.KeeperState state = watchedEvent.getState();
        switch (state) {
            case Unknown:
                break;
            case Disconnected:
                break;
            case NoSyncConnected:
                break;
            case SyncConnected:
                break;
            case AuthFailed:
                break;
            case ConnectedReadOnly:
                break;
            case SaslAuthenticated:
                break;
            case Expired:
                break;
        }

        Event.EventType type = watchedEvent.getType();
        switch (type) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                zk.getChildren("/", false, this, "BASIC");
                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                break;
        }
    }

    /**
     * StringCallBack
     * @param i
     * @param s
     * @param o
     * @param s1
     */
    @Override
    public void processResult(int i, String s, Object o, String s1) {
        if (s1 != null) {
            this.pathName = s1;
            System.out.println(threadName + " create path " + pathName);
            zk.getChildren("/", false, this, "BASIC");
        }
    }

    /**
     * Children2Callback
     * @param i
     * @param s
     * @param o
     * @param list
     * @param stat
     */
    @Override
    public void processResult(int i, String s, Object o, List<String> list, Stat stat) {
        Collections.sort(list);
        int index = list.indexOf(pathName.substring(1));
        if (index == 0) {
            countDownLatch.countDown();
        } else {
            zk.exists("/" + list.get(index - 1), this, this, "BASIC");
        }
    }

    @Override
    public void processResult(int i, String s, Object o, Stat stat) {

    }
}
