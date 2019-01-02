package com.andrei.tcapov.server.service;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public final class ConfigService {

    private static Map<String, String> appProps;

    private ConfigService(){}

    private static ResourceBundle rb = ResourceBundle.getBundle("config");

    public static void init() {
        appProps = new HashMap<>();
        putPropertyToMap(Properties.SERVER_PORT);
        putPropertyToMap(Properties.EXECUTOR_AWAIT_TERMINATION);
        putPropertyToMap(Properties.CACHE_MAX_LIVING_TIME);
        putPropertyToMap(Properties.LOG_FILE_DIR);
        putPropertyToMap(Properties.LOG_FILE_NAME);
        putPropertyToMap(Properties.DB_DRIVER);
        putPropertyToMap(Properties.DB_URL);
        putPropertyToMap(Properties.DB_USER);
        putPropertyToMap(Properties.DB_PASS);
        putPropertyToMap(Properties.DB_MAX_ACTIVE_CONNECTIONS);
    }

    public static int getServerPort() {
        return Integer.valueOf(appProps.get(Properties.SERVER_PORT.val));
    }

    public static long getExecutorAwaitTermination() {
        return Long.valueOf(appProps.get(Properties.EXECUTOR_AWAIT_TERMINATION.val));
    }

    public static long getCacheLivingTime() {
        return Long.valueOf(appProps.get(Properties.CACHE_MAX_LIVING_TIME.val));
    }

    public static String getLogFileDir() {
        return appProps.get(Properties.LOG_FILE_DIR.val);
    }

    public static String getLogFileName() {
        return appProps.get(Properties.LOG_FILE_NAME.val);
    }

    public static String getDbDriver() {
        return appProps.get(Properties.DB_DRIVER.val);
    }

    public static String getDbUrl() {
        return appProps.get(Properties.DB_URL.val);
    }

    public static String getDbUser() {
        return appProps.get(Properties.DB_USER.val);
    }

    public static String getDbPass() {
        return appProps.get(Properties.DB_PASS.val);
    }

    public static int getDbMaxActiveConn() {
        return Integer.valueOf(appProps.get(Properties.DB_MAX_ACTIVE_CONNECTIONS.val));
    }

    private static void putPropertyToMap(Properties prop) {
        String sysProperty = System.getProperty(prop.val);
        appProps.put(prop.val, sysProperty == null ? rb.getString(prop.val) : sysProperty);
    }
}
