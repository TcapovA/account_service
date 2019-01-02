package com.andrei.tcapov.server.service;

import com.andrei.tcapov.server.api.Account;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Cache {

    private ConcurrentMap<Integer, Account> cache;
    private CleanCacheTask cleanTask;
    private Thread thread;
    private final long maxLivingTimeMillis;

    public Cache() {
        maxLivingTimeMillis = ConfigService.getCacheLivingTime();
        cache = new ConcurrentHashMap<>();
        cleanTask = new CleanCacheTask(this);
        thread = new Thread(cleanTask);
        thread.start();
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

    public void cleanCache() {
        long currentDateTime = Instant.now().toEpochMilli();

        cache.values().removeIf(account -> {
            long difference = currentDateTime - account.getLastAccessDate();

            return difference > 0 && difference < maxLivingTimeMillis;
        });
    }

    public void shutdown() {
        thread.interrupt();
    }
}
