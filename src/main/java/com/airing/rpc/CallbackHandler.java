package com.airing.rpc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CallbackHandler {

    static ConcurrentHashMap<Long, Runnable> mapping = new ConcurrentHashMap<>();

    public static void add(Long requestId, Runnable runnable) {
        mapping.putIfAbsent(requestId, runnable);
    }

    public static void remove(Long requestId) {
        mapping.remove(requestId);
    }

    public static void run(Long requestId) {
        mapping.get(requestId).run();
        remove(requestId);
    }

}
