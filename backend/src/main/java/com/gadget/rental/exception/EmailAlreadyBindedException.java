package com.gadget.rental.exception;

public class EmailAlreadyBindedException extends RuntimeException {
    public EmailAlreadyBindedException(String message) {
        super(message);
    }

    public EmailAlreadyBindedException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
