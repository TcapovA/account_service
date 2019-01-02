package com.andrei.tcapov.server.api;

import com.andrei.tcapov.server.service.Logger;

public class UnknownCommand extends Command {

    UnknownCommand(String[] command) {
        super(command);
    }

    @Override
    public String execute() {
        String message = "Unknown command from request: " + commandData;
        Logger.sendMessage(message);
        throw new IllegalArgumentException(message);
    }
}
