package com.andrei.tcapov.server;

import com.andrei.tcapov.server.api.RequestTask;
import com.andrei.tcapov.server.exception.InsufficientFundsException;
import com.andrei.tcapov.server.service.Cache;
import com.andrei.tcapov.server.service.DbService;
import com.andrei.tcapov.server.service.Logger;
import com.andrei.tcapov.server.service.ConfigService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
    private static ThreadPoolExecutor executor;
    private static Cache cache;
    private static ServerSocket serverSocket;
    private static volatile boolean flagStopped = false;

    public static Cache getCache() {
        return cache;
    }

    public static void main(String[] args) throws IOException {
        init();
        handleRequests();
        stopServer();
    }

    private static void init() throws IOException {
        ConfigService.init();
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        cache = new Cache();
        Logger.init();
        DbService.init();
        serverSocket = new ServerSocket(ConfigService.getServerPort());

        cache.cleanCacheStart();

        String serverStartedMsg = "Server has been successfully initialized";
        System.out.println(serverStartedMsg);
        Logger.log(serverStartedMsg);
    }

    private static void handleRequests() {
        do {
            try {
                Socket clientSocket = serverSocket.accept();
                RequestTask task = new RequestTask(clientSocket);
                executor.execute(task);
            } catch (IOException ex) {
                Logger.log(ex);
            }
        } while (!flagStopped);
    }

    private static void stopServer() {
        System.out.println("Trying to shut down the server");
        System.out.println("Trying to shut down an executor");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(ConfigService.getExecutorAwaitTermination(), TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
            System.out.println("Executor has been successfully shut down");
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        System.out.println("Trying to close the socket");
        try {
            serverSocket.close();
            System.out.println("Socket has been closed");
        } catch (IOException ex) {
            Logger.log(ex);
        }
        DbService.shutdown();

        System.out.println("Shutting down cache");
        cache.shutdown();

        System.out.println("Trying to shut down the Logger");
        Logger.log("Trying to shut down the Logger");
        Logger.shutdown();
        System.out.println("Logger has been successfully shut down");

        System.out.println("Server has been successfully shut down");
        System.exit(1);
    }

    public static void setStopFlag() {
        flagStopped = true;
    }
}
