package com.airing.thread.reference;

import com.airing.thread.local.Person;
import java.io.IOException;

public class StrongReferenceTest {

    public static void main(String[] args) throws IOException {
        Person person = new Person();
        person = null;
        System.gc();
        System.in.read();
    }

}
