package com.andrei.tcapov.client.service;

public enum Properties {
    SERVER_HOST("server.host"),
    SERVER_PORT("server.port"),
    READ_CLIENT_NUMBER("rCount"),
    WRITE_CLIENT_NUMBER("wCount"),
    GET_ID_RANGE("idList"),
    ADD_ID_RANGE("idList"),
    ADD_BOTTOM_RANGE("wBottomRange"),
    ADD_UPPER_RANGE("wUpperRange");

    Properties(String val) {
        this.val = val;
    }

    public final String val;
}
