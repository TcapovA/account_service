package com.andrei.tcapov.server.api;

import com.andrei.tcapov.server.service.Logger;

import java.util.Arrays;

public class UnknownCommand extends Command {

    UnknownCommand(String[] command) {
        super(command);
    }

    @Override
    public String execute() {
        String message = "Unknown command from request: " + Arrays.toString(commandData);
        Logger.log(message);
        throw new IllegalArgumentException(message);
    }
}
