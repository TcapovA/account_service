package com.andrei.tcapov.server.api;

import com.andrei.tcapov.server.service.StatisticService;

public class ResetStatisticsCommand extends Command {

    private static final Object object = new Object();

    private StatisticService getStatistics;
    private StatisticService addStatistics;

    public ResetStatisticsCommand(StatisticService getStatistics, StatisticService addStatistics) {
        super(null);
        this.getStatistics = getStatistics;
        this.addStatistics = addStatistics;
    }

    @Override
    public String execute() {
        synchronized (object) {
            getStatistics.reset();
            addStatistics.reset();
        }
        return "Statistics has been successfully reset";
    }
}
