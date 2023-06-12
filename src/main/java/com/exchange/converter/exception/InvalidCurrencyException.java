package com.exchange.converter.exception;

public class InvalidCurrencyException extends Exception {
    public InvalidCurrencyException() {
        super("Invalid currency provided");
    }

    public InvalidCurrencyException(String message) {
        super(message);
    }
}
