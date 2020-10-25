package com.airing.thread.local;

import java.util.concurrent.TimeUnit;

public class ThreadLocalTest02 {

    static ThreadLocal<Person> tl = new ThreadLocal<Person>() {
        @Override
        protected Person initialValue() {
            return new Person();
        }
    };

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println(tl.get().name);
            } catch (Exception e) {

            }
        }).start();

        new Thread(() -> {
            try {

                TimeUnit.SECONDS.sleep(1);
                Person person = new Person();
                person.name = "lisi";
                tl.set(person);
            } catch (Exception e) {

            }
        }).start();
    }

}
