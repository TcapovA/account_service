package com.andrei.tcapov.client.handler;

import com.andrei.tcapov.client.service.IdRange;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Клиент, отправляющий серверу запросы на получение статистики. Формат: {GET_STATISTICS}
 */
public class GetStatisticsClient extends AbstractClient {

    private ScheduledExecutorService scheduledExecutorService;

    public GetStatisticsClient() {
        super();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    protected IdRange getIdRange() {
        return null;
    }

    @Override
    protected String getMethodName() {
        return "GET_STATISTICS";
    }

    @Override
    public void execute() {
        scheduledExecutorService.scheduleAtFixedRate(() -> getTask(null), 4L, 2L, TimeUnit.SECONDS);
    }
}
