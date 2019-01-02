package com.andrei.tcapov.server.service;

import java.util.concurrent.TimeUnit;

public class CleanCacheTask implements Runnable {

    private Cache cache;

    public CleanCacheTask(Cache cache) {
        this.cache = cache;
    }

    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()) {
                TimeUnit.SECONDS.sleep(20);
                cache.cleanCache();
            }
        } catch (InterruptedException ex) {
            // ok, proceed to work
        }
    }
}
