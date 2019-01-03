package com.andrei.tcapov.client.service;

public class IdRange {

    private final int bottom;
    private final int upper;

    public IdRange(int bottom, int upper) {
        this.bottom = bottom;
        this.upper = upper;
    }

    public int getBottom() {
        return bottom;
    }

    public int getUpper() {
        return upper;
    }
}
