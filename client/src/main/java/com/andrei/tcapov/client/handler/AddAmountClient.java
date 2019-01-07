package com.andrei.tcapov.client.handler;


import com.andrei.tcapov.client.service.ConfigService;
import com.andrei.tcapov.client.service.IdRange;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Клиент, отправляющий серверу запросы на изменение баланса счета с заданным id. Формат: {ADD;id;amount}
 */

public class AddAmountClient extends AbstractClient {

    private static final String METHOD = "ADD";

    public AddAmountClient(int n) {
        super(n);
    }

    @Override
    protected IdRange getIdRange() {
        return ConfigService.fetchAddIdRange();
    }

    @Override
    protected String getMethodName() {
        return METHOD;
    }

    @Override
    protected String getRequestString(IdRange idRange) {
        return super.getRequestString(idRange) + DELIMITER +
                ThreadLocalRandom.current().nextInt(idRange.getBottom(), idRange.getUpper() + 1) + DELIMITER +
                ThreadLocalRandom.current().nextInt(ConfigService.getAddBottomRange(), ConfigService.getAddUpperRange());
    }
}
