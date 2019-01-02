package com.andrei.tcapov.server.api;

import com.andrei.tcapov.server.service.StatisticService;

public class StatisticsCommand extends Command {

    private StatisticService getStatistics;
    private StatisticService addStatistics;

    public StatisticsCommand(StatisticService getStatistics, StatisticService addStatistics) {
        super(null);
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
