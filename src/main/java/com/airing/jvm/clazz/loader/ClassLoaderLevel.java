package com.airing.jvm.clazz.loader;

import sun.net.spi.nameservice.dns.DNSNameService;

public class ClassLoaderLevel {

    public static void main(String[] args) {
        System.out.println(String.class.getClassLoader());
        System.out.println(DNSNameService.class.getClassLoader());
        System.out.println(ClassLoaderLevel.class.getClassLoader());

        // ClassLoaderLevel类的类加载器的父加载器
        System.out.println(ClassLoaderLevel.class.getClassLoader().getParent());
        // ClassLoaderLevel类的类加载器类的类加载器
        System.out.println(ClassLoaderLevel.class.getClassLoader().getClass().getClassLoader());

        System.out.println(new CustomClassLoader().getParent());
        System.out.println(new CustomClassLoader().getParent().getParent());
        System.out.println(new CustomClassLoader().getParent().getParent().getParent());
    }

}
