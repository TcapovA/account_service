package com.andrei.tcapov.server.api;

import com.andrei.tcapov.server.service.StatisticService;
import com.andrei.tcapov.server.util.CommonUtils;

public class ResetStatisticsCommand extends Command {

    private StatisticService getStatistics;
    private StatisticService addStatistics;

    ResetStatisticsCommand(StatisticService getStatistics, StatisticService addStatistics) {
        super(CommonUtils.getOneElementStringArray(RequestType.RESET_STATISTICS.val));
        this.getStatistics = getStatistics;
        this.addStatistics = addStatistics;
    }

    @Override
    public String execute() {
        getStatistics.reset();
        addStatistics.reset();
        return "Statistics has been successfully reset";
    }
}
