package com.andrei.tcapov.server.service;

import com.andrei.tcapov.server.api.Account;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Cache {

    private ConcurrentMap<Integer, Account> cache;
    private final long maxLivingTimeSec;

    private final ScheduledExecutorService cleanCacheExecutor;

    public Cache() {
        maxLivingTimeSec = ConfigService.getCacheLivingTime();
        cache = new ConcurrentHashMap<>();

        cleanCacheExecutor = Executors.newSingleThreadScheduledExecutor();
        cleanCacheStart();
    }

    private void cleanCacheStart() {
        cleanCacheExecutor.scheduleWithFixedDelay(this::cleanCache, maxLivingTimeSec, maxLivingTimeSec, TimeUnit.SECONDS);
    }

    public void put(Account account) {
        cache.put(account.getId(), account);
    }

    public Account get(Integer id) {
        Account account = cache.get(id);
        if (account == null) {
            return null;
        }
        return account;
    }

    private void cleanCache() {
        long currentDateTime = Instant.now().toEpochMilli();

        cache.values().removeIf(account -> {
            long difference = currentDateTime - account.getLastAccessDate();

            return difference > 0 && difference < maxLivingTimeSec;
        });
    }

    public void shutdown() {
        cleanCacheExecutor.shutdown();
    }
}
