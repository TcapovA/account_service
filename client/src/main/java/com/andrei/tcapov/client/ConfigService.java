package com.andrei.tcapov.client;

import com.andrei.tcapov.client.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ConfigService {

    private static Map<String, String> appProps;

    private ConfigService(){}

    private static ResourceBundle rb = ResourceBundle.getBundle("config");

    public static void init() {
        appProps = new HashMap<>();
        putPropertyToMap(Properties.SERVER_PORT);
        putPropertyToMap(Properties.SERVER_HOST);
        putPropertyToMap(Properties.READ_CLIENT_NUMBER);
        putPropertyToMap(Properties.WRITE_CLIENT_NUMBER);
        putPropertyToMap(Properties.GET_ID_RANGE);
        putPropertyToMap(Properties.ADD_ID_RANGE);
        putPropertyToMap(Properties.ADD_BOTTOM_RANGE);
        putPropertyToMap(Properties.ADD_UPPER_RANGE);
    }

    public static int getReadClientNumber(){
        return Integer.valueOf(appProps.get(Properties.READ_CLIENT_NUMBER.val));
    }

    public static int getWriteClientNumber(){
        return Integer.valueOf(appProps.get(Properties.WRITE_CLIENT_NUMBER.val));
    }

    public static String getServerHost() {
        return appProps.get(Properties.SERVER_HOST.val);
    }

    public static int getServerPort(){
        return Integer.valueOf(appProps.get(Properties.SERVER_PORT.val));
    }

    public static IdRange fetchGetIdRange(){
        return CommonUtils.parseIdRange(appProps.get(Properties.GET_ID_RANGE.val));
    }

    public static IdRange fetchAddIdRange(){
        return CommonUtils.parseIdRange(appProps.get(Properties.ADD_ID_RANGE.val));
    }

    public static int getAddBottomRange() {
        return Integer.valueOf(appProps.get(Properties.ADD_BOTTOM_RANGE.val));
    }

    public static int getAddUpperRange() {
        return Integer.valueOf(appProps.get(Properties.ADD_UPPER_RANGE.val));
    }

    private static void putPropertyToMap(Properties prop) {
        String sysProperty = System.getProperty(prop.val);
        appProps.put(prop.val, sysProperty == null ? rb.getString(prop.val) : sysProperty);
    }
}
