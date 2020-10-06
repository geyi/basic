package com.airing.rpc.v3.rpc.protocol;

import java.io.Serializable;

public class Header implements Serializable {
    private int code;
    private long requestId;
    private long bodyLength;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public long getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(long bodyLength) {
        this.bodyLength = bodyLength;
    }
}
