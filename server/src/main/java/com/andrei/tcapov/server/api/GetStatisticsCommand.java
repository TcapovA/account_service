package com.andrei.tcapov.server.api;

import com.andrei.tcapov.server.service.StatisticService;
import com.andrei.tcapov.server.util.CommonUtils;

public class GetStatisticsCommand extends Command {

    private StatisticService getStatistics;
    private StatisticService addStatistics;

    GetStatisticsCommand(StatisticService getStatistics, StatisticService addStatistics) {
        super(CommonUtils.getOneElementStringArray(RequestType.GET_STATISTICS.val));
        this.getStatistics = getStatistics;
        this.addStatistics = addStatistics;
    }

    @Override
    public String execute() {
        return String.format("Statistics:\nget requests: total amount: %s ; requests per second: %s\n" +
                "add requests: total amount: %s ; requests per second: %s", getStatistics.getRequestCounter(),
                getStatistics.getRequestPerSecond(), addStatistics.getRequestCounter(), addStatistics.getRequestPerSecond());
    }
}
