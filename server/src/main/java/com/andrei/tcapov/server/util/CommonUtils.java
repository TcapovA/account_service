package com.andrei.tcapov.server.util;

public final class CommonUtils {

    private CommonUtils() {
    }

    public static String[] getOneElementStringArray(String element) {
        String[] mass = new String[1];
        mass[0] = element;
        return mass;
    }
}
