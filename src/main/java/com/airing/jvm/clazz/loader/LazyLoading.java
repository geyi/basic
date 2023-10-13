package com.airing.jvm.clazz.loader;

public class LazyLoading {
    static {
        System.out.println("lazy loading");
    }

    public static void main(String[] args) throws ClassNotFoundException {
        // P类不会被加载
//        P p;
        // P类不会被加载
//        System.out.println(P.i);
        // P类会被加载
//        System.out.println(P.j);
        // P类会被加载
//        P.j = 0;
        // P类会被加载
//        X x = new X();
        // P类会被加载
//        Class.forName("com.airing.clazzloader.LazyLoading$P");
        // P类会被加载
        P.test();
    }

    public static class P {
        public final static int i = 8;
        public static int j = 9;
        static {
            System.out.println("p");
        }

        public static void test() {}
    }

    public static class X extends P {
        static {
            System.out.println("x");
        }
    }
}
