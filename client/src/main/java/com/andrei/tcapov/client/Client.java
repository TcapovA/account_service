package com.andrei.tcapov.client;

import com.andrei.tcapov.client.handler.AddAmountClient;
import com.andrei.tcapov.client.handler.ResetStatisticsClient;
import com.andrei.tcapov.client.handler.StopClient;
import com.andrei.tcapov.client.service.ConfigService;
import com.andrei.tcapov.client.handler.GetAmountClient;
import com.andrei.tcapov.client.handler.GetStatisticsClient;

public class Client {

    // for future usage
    private static volatile boolean flagInterruptApp = false;

    private static GetAmountClient getAmountClient;
    private static AddAmountClient addAmountClient;
    private static GetStatisticsClient getStatisticClient;
    private static ResetStatisticsClient clearStatisticClient;
    private static StopClient stopClient;


    public static void main(String[] args) {
        init();
        sendRequests();
    }

    public static void stopApp(){
        flagInterruptApp = true;
    }

    private static void init() {
        ConfigService.init();
        getAmountClient = new GetAmountClient(ConfigService.getReadClientNumber());
        addAmountClient = new AddAmountClient(ConfigService.getWriteClientNumber());
        getStatisticClient = new GetStatisticsClient();
        clearStatisticClient = new ResetStatisticsClient();
        stopClient = new StopClient();
    }

    private static void sendRequests() {
        clearStatisticClient.execute();
        stopClient.execute();
        getStatisticClient.execute();

        while (!flagInterruptApp) {
            getAmountClient.execute();
            addAmountClient.execute();
        }
    }
}
