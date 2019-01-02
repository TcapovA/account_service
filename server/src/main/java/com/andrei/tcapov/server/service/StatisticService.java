package com.andrei.tcapov.server.service;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

public class StatisticService {

    public StatisticService() {
    }

    private AtomicLong requestCounter = new AtomicLong(0L);
    private Instant startTimePoint = Instant.now();
    private volatile boolean resetFlag = false;

    public synchronized void reset() {
        resetFlag = true;
        requestCounter.set(0);
        startTimePoint = Instant.now();
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
        return requestCounter.doubleValue() / (((double)Instant.now().toEpochMilli() - startTimePoint.toEpochMilli()) / 1000);
    }

    public void incrementRequestCounter() {
        requestCounter.incrementAndGet();
    }
}
