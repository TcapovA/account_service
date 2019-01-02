package com.andrei.tcapov.server.api;

import com.andrei.tcapov.server.service.Logger;
import com.andrei.tcapov.server.service.StatisticService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class RequestTask implements Runnable {

    private static final String DELIMITER = ";";

    private static final StatisticService getStatisticService = new StatisticService();
    private static final StatisticService addStatisticService = new StatisticService();

    private Socket clientSocket;

    public RequestTask(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String line = in.readLine();
            Logger.sendMessage(line);

            String[] requestData = line.split(DELIMITER);
            RequestType requestType = getRequestType(requestData);
            Command command;

            switch (requestType) {
                case GET:
                    command = new GetCommand(requestData);
                    break;
                case ADD:
                    command = new AddCommand(requestData);
                    break;
                case STOP:
                    command = new StopCommand(requestData);
                    break;
                case STATISTIC:
                    command = new StatisticsCommand(getStatisticService, addStatisticService);
                    break;
                case RESET_STATISTICS:
                    command = new ResetStatisticsCommand(getStatisticService, addStatisticService);
                    break;
                case UNKNOWN:
                    command = new UnknownCommand(requestData);
                    break;
                default:
                    // shouldn't be here
                    String message = "Internal error while handling request: " + line;
                    Logger.sendMessage(message);
                    throw new IllegalArgumentException(message);
            }
            String res = command.execute();

            String msg = "result of " + command + ": " + res;
            Logger.sendMessage(msg);
            outputStream.writeBytes(res + "\r\n");
            outputStream.flush();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            Logger.sendMessage(ex.getMessage());
        } catch (Exception ex) {
            // Add logging stack trace
            Logger.sendMessage("Error while trying to handle request" + Arrays.toString(ex.getStackTrace()));
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ioEx) {
                Logger.sendMessage(Arrays.toString(ioEx.getStackTrace()));
            }
        }
    }

    private static RequestType getRequestType(String[] requestData) {
        if (requestData == null || requestData.length == 0) {
            return RequestType.UNKNOWN;
        }

        String type = requestData[0];

        if (type.equals(RequestType.GET.val)) {
            getStatisticService.incrementRequestCounter();
            return RequestType.GET;
        } else if (type.equals(RequestType.ADD.val)) {
            addStatisticService.incrementRequestCounter();
            return RequestType.ADD;
        } else if (type.equals(RequestType.STATISTIC.val)) {
            return RequestType.STATISTIC;
        } else if (type.equals(RequestType.RESET_STATISTICS.val)) {
            return RequestType.RESET_STATISTICS;
        } else {
            return RequestType.UNKNOWN;
        }
    }
}
