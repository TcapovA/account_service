package com.andrei.tcapov.server.service;

import com.andrei.tcapov.server.api.Account;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CacheService {

    private final ConcurrentMap<Integer, Account> cacheHolder;
    private final long maxLivingTimeSec;

    private final ScheduledExecutorService cleanCacheExecutor;

    public CacheService() {
        maxLivingTimeSec = ConfigService.getCacheLivingTime();
        cacheHolder = new ConcurrentHashMap<>();

        cleanCacheExecutor = Executors.newSingleThreadScheduledExecutor(new ThreadDeamonFactory());
    }

    public void cleanCacheStart() {
        cleanCacheExecutor.scheduleAtFixedRate(this::cleanCache, maxLivingTimeSec, maxLivingTimeSec, TimeUnit.SECONDS);
    }

    public void put(Account account) {
        cacheHolder.put(account.getId(), account);
    }

    public Account get(Integer id) {
        Account account = cacheHolder.get(id);
        if (account == null) {
            return null;
        }
        return account;
    }

    private void cleanCache() {
        long currentDateTime = System.currentTimeMillis();

        cacheHolder.values().removeIf(account -> {
            long difference = TimeUnit.MILLISECONDS.toSeconds(currentDateTime - account.getLastAccessDate());

            return difference > 0 && difference < maxLivingTimeSec;
        });
    }

    public void shutdown() {
        cleanCacheExecutor.shutdown();
    }
}
