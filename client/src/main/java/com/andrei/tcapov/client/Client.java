package com.andrei.tcapov.client;

public class Client {

    // for future usage
    private static volatile boolean flagStopped = false;

    private static GetAmountClient getAmountClient;
    private static AddAmountClient addAmountClient;
    private static GetStatisticsClient getStatisticClient;

    public static void main(String[] args) {
        init();
        sendRequests();
    }

    private static void init() {
        ConfigService.init();
        getAmountClient = new GetAmountClient(ConfigService.getReadClientNumber());
        addAmountClient = new AddAmountClient(ConfigService.getWriteClientNumber());
        getStatisticClient = new GetStatisticsClient();
    }

    private static void sendRequests() {
        while (!flagStopped) {
            getAmountClient.execute();
            addAmountClient.execute();
            getStatisticClient.execute();
        }
    }
}
