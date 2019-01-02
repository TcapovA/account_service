package com.andrei.tcapov.server.api;

import com.andrei.tcapov.server.Server;
import com.andrei.tcapov.server.service.Cache;
import com.andrei.tcapov.server.service.DbService;
import com.andrei.tcapov.server.service.Logger;

import java.sql.SQLException;
import java.util.Arrays;

public class AddCommand extends Command {

    AddCommand(String[] command) {
        super(command);
    }

    @Override
    public String execute() {
        AddAmountRequest addRequest = convertToAddRequest(commandData);
        Cache cache = Server.getCache();
        Account account = cache.get(addRequest.getId());
        try {
            if (account == null) {
                return DbService.createAccount(addRequest.getId(), addRequest.getAmount());
            } else {
                return DbService.updateAmount(addRequest.getId(), addRequest.getAmount() + account.getAmount());
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Exception while trying to update account with id = addRequest.getId()", e);
        }
    }

    private static AddAmountRequest convertToAddRequest(String[] requestData) {
        try {
            Integer id = Integer.parseInt(requestData[1]);
            Long amount = Long.parseLong(requestData[2]);
            return new AddAmountRequest(id, amount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Bad message with params: " + requestData);
        }
    }
}
