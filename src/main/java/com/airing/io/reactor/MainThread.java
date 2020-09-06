package com.airing.io.reactor;

public class MainThread {

    public static void main(String[] args) {
        SelectorThreadGroup stg = new SelectorThreadGroup(3);
        stg.bind(9000);
    }

}
