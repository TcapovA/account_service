package com.andrei.tcapov.client;


import java.util.concurrent.ThreadLocalRandom;

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
