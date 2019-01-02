package com.andrei.tcapov.server.service;

public enum Properties {
    SERVER_PORT("server.port"),
    EXECUTOR_AWAIT_TERMINATION("executor.await.termination"),
    CACHE_MAX_LIVING_TIME("cache.max.living.time"),
    LOG_FILE_DIR("log.file.path"),
    LOG_FILE_NAME("log.file.name"),
    DB_DRIVER("db.driver"),
    DB_URL("db.url"),
    DB_USER("db.user"),
    DB_PASS("db.password"),
    DB_MAX_ACTIVE_CONNECTIONS("db.max.active.connections");

    Properties(String val) {
        this.val = val;
    }

    public final String val;
}
