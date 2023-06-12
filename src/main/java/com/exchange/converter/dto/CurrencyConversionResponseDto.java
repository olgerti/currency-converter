package com.exchange.converter.dto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrencyConversionResponseDto {

    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal amount;
    private BigDecimal convertedAmount;
    private BigDecimal exchangeRate;

    private ErrorResponseDto error;

    public CurrencyConversionResponseDto() {
    }

    public CurrencyConversionResponseDto(String baseCurrency,
                                         String targetCurrency, BigDecimal amount,
                                         BigDecimal convertedAmount, BigDecimal exchangeRate) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
        this.exchangeRate = exchangeRate;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public ErrorResponseDto getError() {
        return error;
    }

    public void setError(ErrorResponseDto error) {
        this.error = error;
    }
}
