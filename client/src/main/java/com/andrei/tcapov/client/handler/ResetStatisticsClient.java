package com.andrei.tcapov.client.handler;

import com.andrei.tcapov.client.service.IdRange;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ResetStatisticsClient extends AbstractClient {

    private ScheduledExecutorService scheduledExecutorService;

    public ResetStatisticsClient() {
        super();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    protected IdRange getIdRange() {
        return null;
    }

    @Override
    protected String getMethodName() {
        return "RESET_STATISTICS";
    }

    @Override
    public void execute() {
        scheduledExecutorService.schedule(() -> getTask(null), 10, TimeUnit.SECONDS);
    }
}
