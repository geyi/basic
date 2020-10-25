package com.airing.thread.reference;

import java.lang.ref.SoftReference;
import java.util.concurrent.TimeUnit;

public class SoftReferenceTest {

    public static void main(String[] args) throws Exception {
        SoftReference<byte[]> sr = new SoftReference<>(new byte[1204 * 1024 * 10]);
        System.out.println(sr.get());

        System.gc();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(sr.get());

        byte[] bytes = new byte[1204 * 1024 * 10];
        System.out.println(sr.get());
    }

}
