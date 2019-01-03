package com.andrei.tcapov.client.handler;


import com.andrei.tcapov.client.service.ConfigService;
import com.andrei.tcapov.client.service.IdRange;

import java.util.concurrent.ThreadLocalRandom;

public class GetAmountClient extends AbstractClient {

    private static final String METHOD = "GET";

    public GetAmountClient(int n) {
        super(n);
    }

    @Override
    protected IdRange getIdRange() {
        return ConfigService.fetchGetIdRange();
    }

    @Override
    protected String getMethodName() {
        return METHOD;
    }

    @Override
    protected String getRequestString(IdRange idRange) {
        return getMethodName() + DELIMITER + ThreadLocalRandom.current().nextInt(idRange.getBottom(),
                idRange.getUpper() + 1);
    }
}
