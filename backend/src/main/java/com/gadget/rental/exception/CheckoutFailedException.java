package com.gadget.rental.exception;

public class CheckoutFailedException extends RuntimeException {

    public CheckoutFailedException(String message) {
        super(message);
    }

    public CheckoutFailedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
