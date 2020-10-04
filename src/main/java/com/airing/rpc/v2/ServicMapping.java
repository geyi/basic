package com.airing.rpc.v2;

import java.util.concurrent.ConcurrentHashMap;

public class ServicMapping {

    private static ServicMapping instance = new ServicMapping();
    private static ConcurrentHashMap<String, Object> mapping = new ConcurrentHashMap<>();

    private ServicMapping() {

    }

    public static ServicMapping getInstance() {
        return instance;
    }

    static {
        mapping.putIfAbsent(RpcService.class.getName(), new RpcServiceImpl());
    }

    public Object get(String serviceName) {
        return mapping.get(serviceName);
    }

}
