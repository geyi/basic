package com.airing.object.header;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

public class TestMain {

    public static void main(String[] args) {
        A a = new A();
        System.out.println(VM.current().details());
        System.out.println(ClassLayout.parseInstance(a).toPrintable());
    }

}
