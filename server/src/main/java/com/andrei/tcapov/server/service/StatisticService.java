package com.andrei.tcapov.server.service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class StatisticService {

    private AtomicLong requestCounter = new AtomicLong(0L);
    private volatile long startTimePoint = System.nanoTime();
    private volatile boolean resetFlag = false;

    public StatisticService() {
    }

    public synchronized void reset() {
        resetFlag = true;
        requestCounter.set(0);
        startTimePoint = System.nanoTime();
        resetFlag = false;
    }

    public Long getRequestCounter() {
        if (resetFlag) {
            return 0L;
        }
        return requestCounter.longValue();
    }

    public double getRequestPerSecond() {
        if (resetFlag) {
            return 0;
        }
        return requestCounter.doubleValue() / (TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTimePoint));
    }

    public void incrementRequestCounter() {
        requestCounter.incrementAndGet();
    }
}
