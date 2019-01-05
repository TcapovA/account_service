package com.andrei.tcapov.client.util;

import com.andrei.tcapov.client.service.IdRange;

public class CommonUtils {

    private static final String RANGE_DELIMITER = ";";

    private CommonUtils(){}

    public static IdRange parseIdRange(String input) {
        String[] mass = input.split(RANGE_DELIMITER);
        if (mass.length != 2) {
            throw new IllegalArgumentException("Invalid id range: " + input);
        }

        return new IdRange(Integer.valueOf(mass[0]), Integer.valueOf(mass[1]));
    }
}
