package com.airing.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockTest {
    static ReentrantLock lock = new ReentrantLock();
    static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    static ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    static ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();

    static void read(Lock lock) {
        try {
            lock.lock();
            TimeUnit.SECONDS.sleep(1);
            System.out.println("read over");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    static void write(Lock lock, int value) {
        try {
            lock.lock();
            TimeUnit.SECONDS.sleep(1);
            System.out.println("write over");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> ReentrantReadWriteLockTest.read(readLock)).start();
        }
        for (int i = 0; i < 2; i++) {
            new Thread(() -> ReentrantReadWriteLockTest.write(writeLock, 1)).start();
        }
    }
}
