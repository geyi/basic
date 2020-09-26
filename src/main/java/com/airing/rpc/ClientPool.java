package com.airing.rpc;

import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientPool {
    NioSocketChannel[] clients;
    Object[] locks;

    public ClientPool(int poolsize) {
        clients = new NioSocketChannel[poolsize];
        locks = new Object[poolsize];
        for (int i = 0; i < poolsize; i++) {
            locks[i] = new Object();
        }
    }
}
