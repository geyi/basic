package com.airing.jvm.clazz.loader;

public class ClassLoaderScope {

    public static void main(String[] args) {
        String bootClassPath = System.getProperty("sun.boot.class.path");
        System.out.println(bootClassPath.replaceAll(":", System.lineSeparator()));
        System.out.println("------------");
        String extClassPath = System.getProperty("java.ext.dirs");
        System.out.println(extClassPath.replaceAll(":", System.lineSeparator()));
        System.out.println("------------");
        String appClassPath = System.getProperty("java.class.path");
        System.out.println(appClassPath.replaceAll(":", System.lineSeparator()));
        System.out.println("------------");
    }

}
