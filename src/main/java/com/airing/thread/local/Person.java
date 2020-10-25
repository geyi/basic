package com.airing.thread.local;

public class Person {
    String name;

    @Override
    protected void finalize() throws Throwable {
        System.out.println("finalize invoked");
    }
}
