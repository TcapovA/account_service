package com.andrei.tcapov.server.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class DeamonThreadFactory implements ThreadFactory {

    public DeamonThreadFactory() {
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
    }
}
