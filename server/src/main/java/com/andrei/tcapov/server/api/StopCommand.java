package com.andrei.tcapov.server.api;

import com.andrei.tcapov.server.Server;

public class StopCommand extends Command {

    StopCommand(String[] command) {
        super(command);
    }

    @Override
    public String execute() {
        Server.setStopFlag();
        return "Server stopped";
    }
}
