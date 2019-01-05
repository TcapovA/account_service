package com.andrei.tcapov.server.api;

import com.andrei.tcapov.server.Server;
import com.andrei.tcapov.server.service.Cache;
import com.andrei.tcapov.server.service.DbService;
import com.andrei.tcapov.server.service.Logger;

import java.sql.SQLException;
import java.time.Instant;

public class GetCommand extends Command {

    GetCommand(String[] commandData) {
        super(commandData);
    }

    @Override
    public String execute() {
        GetAmountRequest getRequest = convertToGetRequest(commandData);
        Cache cache = Server.getCache();
        Account account = cache.get(getRequest.getId());

        if (account == null) {
            String message = "Account with id = " + getRequest.getId() + " not found in cache";
            Logger.log(message);
            try {
                long amount = DbService.getAmount(getRequest.getId());
                return String.valueOf(amount);
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }
        } else {
            account.setLastAccessDate(Instant.now().toEpochMilli());
            cache.put(account);
        }

        return String.valueOf(account.getAmount());
    }

    private static GetAmountRequest convertToGetRequest(String[] commandData) {
        try {
            return new GetAmountRequest(Integer.parseInt(commandData[1]));
        } catch (NumberFormatException e) {
            String message = "Bad message with params: " + commandData;
            Logger.log(message);
            throw new IllegalArgumentException(message);
        }
    }
}
