package com.airing.rpc.v3.service.impl;

import com.airing.rpc.v3.service.RpcService;

public class RpcServiceImpl implements RpcService {
    @Override
    public String rpcMethod(String param) {
        return "server response " + param;
    }
}
