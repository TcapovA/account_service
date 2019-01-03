package com.andrei.tcapov.client.handler;

import com.andrei.tcapov.client.service.ConfigService;
import com.andrei.tcapov.client.service.IdRange;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class AbstractClient {

    protected static final String DELIMITER = ";";

    private ThreadPoolExecutor executor;

    protected AbstractClient() {
    }

    public AbstractClient(int n) {
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(n);
    }

    public void execute() {
        IdRange idRange = getIdRange();
        executor.execute( () -> getTask(idRange) );
    }

    protected void getTask(IdRange idRange) {
        try (Socket clientSocket = new Socket(ConfigService.getServerHost(), ConfigService.getServerPort());
             DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
             BufferedReader inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            System.out.println(" *** Connected to server ***");
            String req = getRequestString(idRange);
            outputStream.writeBytes(req + "\r\n");
            outputStream.flush();
            System.out.println("Client send message: " + req);

            String getAmountRes;
            while ((getAmountRes = inputStream.readLine()) != null) {
                System.out.println("Client received msg: " + getAmountRes);
            }
        } catch (UnknownHostException e) {
            System.out.println("Unknown server host: " + ConfigService.getServerHost());
        } catch (IOException e) {
            System.out.println("Couldn't get I/O for the connection to: " + ConfigService.getServerHost());
        }
    }

    protected String getRequestString(IdRange idRange) {
        return getMethodName();
    }

    protected abstract IdRange getIdRange();

    protected abstract String getMethodName();
}
