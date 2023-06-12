package com.exchange.converter.exception;

public class InvalidAmountException extends Exception {

    public InvalidAmountException() {
        super("Invalid amount provided");
    }

    public InvalidAmountException(String message) {
        super(message);
    }
}
