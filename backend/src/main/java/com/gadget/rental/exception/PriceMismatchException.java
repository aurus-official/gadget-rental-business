package com.gadget.rental.exception;

public class PriceMismatchException extends RuntimeException {
    public PriceMismatchException(String message) {
        super(message);
    }

    public PriceMismatchException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
