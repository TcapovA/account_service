package com.andrei.tcapov.server.api;

public enum RequestType {
    GET("GET"),
    ADD("ADD"),
    STATISTIC("STATISTIC"),
    RESET_STATISTICS("RESET_STATISTICS"),
    STOP("STOP"),
    UNKNOWN("UNKNOWN");

    RequestType(String type) {
        this.val = type;
    }

    public final String val;
}
