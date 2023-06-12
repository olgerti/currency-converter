package com.exchange.converter.service;

import com.exchange.converter.cache.CurrencyCache;
import com.exchange.converter.exception.InvalidAmountException;
import com.exchange.converter.exception.InvalidCurrencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyValidationService {

    @Autowired
    private CurrencyCache currencyCache;

    public boolean validateCurrency(String currencyCode) throws InvalidCurrencyException {
        if (!currencyCache.isValidCurrency(currencyCode))
            throw new InvalidCurrencyException();

        return true;
    }


    public boolean validateAmount(BigDecimal amount) throws InvalidAmountException {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }

        return true;
    }
}
