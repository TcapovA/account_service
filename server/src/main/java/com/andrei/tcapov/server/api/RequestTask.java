package com.andrei.tcapov.server.api;

import com.andrei.tcapov.server.exception.InsufficientFundsException;
import com.andrei.tcapov.server.service.Logger;
import com.andrei.tcapov.server.service.StatisticService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RequestTask implements Runnable {

    private static final String DELIMITER = ";";
    private static final String END_OF_MESSAGE = "\r\n";

    private static final StatisticService getStatisticService = new StatisticService();
    private static final StatisticService addStatisticService = new StatisticService();

    private Socket clientSocket;

    public RequestTask(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        DataOutputStream outputStream = null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
            String line = in.readLine();
            Logger.log(line);

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
                case GET_STATISTICS:
                    command = new GetStatisticsCommand(getStatisticService, addStatisticService);
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
                    Logger.log(message);
                    throw new IllegalArgumentException(message);
            }
            String res = command.execute();

            String msg = "result of " + command + ": " + res;
            Logger.log(msg);
            outputStream.writeBytes(res + END_OF_MESSAGE);
            outputStream.flush();
        } catch (IllegalArgumentException | InsufficientFundsException ex) {
            Logger.log(ex.getMessage());
            try {
                if (outputStream != null) {
                    outputStream.writeBytes(ex.getMessage() + END_OF_MESSAGE);
                }
            } catch (IOException e) {
                Logger.log(e);
            }
        } catch (Exception ex) {
            Logger.log("Error while trying to handle request" + ex);
        } finally {
            try{
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                Logger.log(e);
            }
            try {
                clientSocket.close();
            } catch (IOException ioEx) {
                Logger.log(ioEx);
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
        }
        if (type.equals(RequestType.ADD.val)) {
            addStatisticService.incrementRequestCounter();
            return RequestType.ADD;
        }
        if (type.equals(RequestType.GET_STATISTICS.val)) {
            return RequestType.GET_STATISTICS;
        }
        if (type.equals(RequestType.RESET_STATISTICS.val)) {
            return RequestType.RESET_STATISTICS;
        }
        if (type.equals(RequestType.STOP.val)) {
            return RequestType.STOP;
        }
        return RequestType.UNKNOWN;
    }
}
