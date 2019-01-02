package com.andrei.tcapov.server.api;

public class AddAmountRequest extends BaseRequest {

    private Long amount;

    AddAmountRequest(Integer id, Long amount) {
        super(id);
        this.amount = amount;
    }

    public Long getAmount() {
        return amount;
    }
}
