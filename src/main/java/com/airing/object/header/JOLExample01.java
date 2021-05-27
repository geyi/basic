package com.airing.object.header;

import org.openjdk.jol.info.ClassLayout;

public class JOLExample01 {

    public static void main(String[] args) {
        A a = new A();
        // 没有计算HashCode之前的对象头
        System.out.println("before hash");
        System.out.println(ClassLayout.parseInstance(a).toPrintable());

        // jvm计算HashCode
        System.out.println("jvm----------" + Integer.toHexString(a.hashCode()));

        // 当计算完HashCode之后，我们可以查看对象头的信息变化
        System.out.println("after hash");
        System.out.println(ClassLayout.parseInstance(a).toPrintable());
    }

}
