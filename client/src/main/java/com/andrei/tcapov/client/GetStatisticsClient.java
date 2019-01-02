package com.andrei.tcapov.client;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GetStatisticsClient extends AbstractClient {

    private ScheduledExecutorService scheduledExecutorService;

    GetStatisticsClient() {
        super();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    protected IdRange getIdRange() {
        return null;
    }

    @Override
    protected String getMethodName() {
        return "STATISTIC";
    }

    @Override
    public void execute() {
        scheduledExecutorService.schedule(() -> getTask(null), 3L, TimeUnit.SECONDS);
    }
}
