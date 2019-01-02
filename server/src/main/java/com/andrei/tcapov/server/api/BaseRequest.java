package com.andrei.tcapov.server.api;

public abstract class BaseRequest {

    public BaseRequest(Integer id) {
        this.id = id;
    }

    private Integer id;

    public Integer getId() {
        return id;
    }
}
