package com.exchange.converter.dto;

import java.util.Map;

public class CurrencyApiDto {
    private Map<String, String> currencies;
    private boolean success;

    public Map<String, String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Map<String, String> currencies) {
        this.currencies = currencies;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}

