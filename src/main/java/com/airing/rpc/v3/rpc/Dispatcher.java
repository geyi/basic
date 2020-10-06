package com.airing.rpc.v3.rpc;

import java.util.concurrent.ConcurrentHashMap;

public class Dispatcher {

    private static Dispatcher instance = new Dispatcher();
    private static ConcurrentHashMap<String, Object> mapping = new ConcurrentHashMap<>();

    private Dispatcher() {

    }

    public static Dispatcher getInstance() {
        return instance;
    }

    public void regist(String serviceName, Object service) {
        mapping.putIfAbsent(serviceName, service);
    }

    public Object get(String serviceName) {
        return mapping.get(serviceName);
    }

}
