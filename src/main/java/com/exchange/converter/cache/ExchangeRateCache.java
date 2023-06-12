package com.exchange.converter.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Component
public class ExchangeRateCache {

    private static final Integer CACHE_DURATION = 6;
    private final Cache<String, BigDecimal> cache;

    public ExchangeRateCache() {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(CACHE_DURATION, TimeUnit.HOURS)
                .build();
    }

    public BigDecimal getExchangeRate(String key) {
        return cache.getIfPresent(key);
    }

    public void putExchangeRate(String key, BigDecimal exchangeRate) {
        cache.put(key, exchangeRate);
    }
}
