package com.airing.rpc.v3.rpc;

import com.airing.rpc.v3.rpc.protocol.Package;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class CallbackHandler {

    static ConcurrentHashMap<Long, CompletableFuture<String>> mapping = new ConcurrentHashMap<>();

    public static void add(Long requestId, CompletableFuture cf) {
        mapping.putIfAbsent(requestId, cf);
    }

    public static void remove(Long requestId) {
        mapping.remove(requestId);
    }

    public static void run(Package pkg) {
        mapping.get(pkg.getHeader().getRequestId()).complete(pkg.getBody().getResponse());
        remove(pkg.getHeader().getRequestId());
    }

}
