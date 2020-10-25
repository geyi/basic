package com.airing.thread.reference;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class WeakReferenceTest {

    public static void main(String[] args) throws Exception {
        WeakReference<byte[]> wr = new WeakReference<>(new byte[1204 * 1024 * 10]);
        System.out.println(wr.get());

        System.gc();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(wr.get());
    }

}
