package com.andrei.tcapov.client.handler;

import com.andrei.tcapov.client.Client;
import com.andrei.tcapov.client.service.IdRange;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StopClient extends AbstractClient {

    private ScheduledExecutorService scheduledExecutorService;

    public StopClient() {
        super();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    protected IdRange getIdRange() {
        return null;
    }

    @Override
    protected String getMethodName() {
        return "STOP";
    }

    @Override
    public void execute() {
        Runnable stopRunnable = () -> {
            getTask(null);
            Client.stopApp();
        };
        scheduledExecutorService.schedule(stopRunnable, 21L, TimeUnit.SECONDS);
    }
}
