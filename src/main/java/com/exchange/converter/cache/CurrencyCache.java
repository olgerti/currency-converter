package com.exchange.converter.cache;

import com.exchange.converter.dto.CurrencyApiDto;
import com.exchange.converter.service.ExternalApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

@Component
public class CurrencyCache {
    private final ExternalApiService externalApiService;
    private final Set<String> allowedCurrencies;

    @Autowired
    public CurrencyCache(ExternalApiService externalApiService) throws URISyntaxException {
        this.externalApiService = externalApiService;
        allowedCurrencies = fetchAllowedCurrencies();
    }

    public boolean isValidCurrency(String currencyCode) {
        return allowedCurrencies.contains(currencyCode);
    }

    private Set<String> fetchAllowedCurrencies() throws URISyntaxException {
        Set<String> currencies = new HashSet<>();
        CurrencyApiDto currencyApiDto = this.externalApiService.getAllowedCurrency();

        currencyApiDto.getCurrencies().forEach((currency, description) -> {
            currencies.add(currency);
        });

        return currencies;
    }
}
