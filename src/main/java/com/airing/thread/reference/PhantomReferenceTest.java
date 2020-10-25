package com.airing.thread.reference;

import com.airing.thread.local.Person;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;

public class PhantomReferenceTest {

    public static void main(String[] args) {
        List<byte[]> list = new ArrayList<>();
        ReferenceQueue<Person> queue = new ReferenceQueue<>();

        PhantomReference<Person> pr = new PhantomReference<>(new Person(), queue);

        new Thread(() -> {
            while (true) {
                list.add(new byte[1024 * 1024]);
                System.out.println(pr.get());
            }
        }).start();

        new Thread(() -> {
            while (true) {
                Reference<? extends Person> poll = queue.poll();
                if (poll != null) {
                    System.out.println("有对象被回收了" + poll);
                }
            }
        }).start();
    }

}
