package com.andrei.tcapov.server.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Logger {

    private static final String DOUBLE_BACKSLASH = "//";

    private static ConcurrentLinkedQueue<String> logQueue = new ConcurrentLinkedQueue<>();
    private static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();;

    public static void log(String message) {
        logQueue.offer(LocalDateTime.now() + ": " + message);
    }

    public static void log(Exception ex) {
        log(Arrays.toString(ex.getStackTrace()));
    }

    private static void writeLogs() {
        String message;
        Path path = Paths.get(ConfigService.getLogFileDir() + DOUBLE_BACKSLASH + ConfigService.getLogFileName());
        try (BufferedWriter fileWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            while ((message = logQueue.poll()) != null) {
                fileWriter.write(message);
                fileWriter.newLine();
            }
        } catch (IOException ex) {
            // skip
            Logger.log(ex);
        }
    }

    public static void init() {
        Path path = Paths.get(ConfigService.getLogFileDir(), ConfigService.getLogFileName());
        if (!path.toFile().exists()) {
            try {
                File dir = new File(ConfigService.getLogFileDir());
                dir.mkdirs();
                File logFile = new File(dir, ConfigService.getLogFileName());
                logFile.createNewFile();
            } catch (IOException e) {
                Logger.log(e);
                // skip
            }
        }
        else {
            try (OutputStream out = Files.newOutputStream(path, StandardOpenOption.TRUNCATE_EXISTING)) {

            } catch (IOException ex) {
                Logger.log(ex);
            }
        }
        logQueue.offer("App launched at " + LocalDateTime.now());
        executor.scheduleWithFixedDelay(Logger::writeLogs, 0, 250, TimeUnit.MILLISECONDS);
    }

    public static void shutdown() {
        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
