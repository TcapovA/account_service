package com.andrei.tcapov.server.api;

import java.util.Arrays;

public abstract class Command {
    protected String[] commandData;

    public Command(String[] command) {
        this.commandData = command;
    }

    public abstract String execute();

    @Override
    public String toString() {
        return "Command{" +
                "commandData=" + Arrays.toString(commandData) +
                '}';
    }
}
