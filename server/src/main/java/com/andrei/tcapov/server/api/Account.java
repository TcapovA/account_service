package com.andrei.tcapov.server.api;

import java.time.Instant;
import java.util.Objects;

public class Account {

    public Account(Integer id, Long amount) {
        this.id = id;
        this.amount = amount;
        lastAccessDate = Instant.now().toEpochMilli();
    }

    /**
     * Идентификатор счета
     */
    private Integer id;

    /**
     * Сумма на счете
     */
    private Long amount;

    private long lastAccessDate;

    public long getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(long lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public Integer getId() {
        return id;
    }

    public Long getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
