package com.exchange.converter.dto;

import java.math.BigDecimal;
import java.util.Map;


public class ConversionRateResponseDto {
    private Map<String, BigDecimal> quotes;
    private String source;
    private boolean success;
    private long timestamp;


    public Map<String, BigDecimal> getQuotes() {
        return quotes;
    }

    public void setQuotes(Map<String, BigDecimal> quotes) {
        this.quotes = quotes;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
