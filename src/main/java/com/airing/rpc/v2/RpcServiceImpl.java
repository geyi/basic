package com.airing.rpc.v2;

public class RpcServiceImpl implements RpcService {
    @Override
    public String rpcMethod(String param) {
        return "server response " + param;
    }
}
