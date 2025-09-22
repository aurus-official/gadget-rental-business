package com.gadget.rental.exception;

public class EmailAlreadyVerifiedException extends RuntimeException {
    public EmailAlreadyVerifiedException(String message) {
        super(message);
    }

    public EmailAlreadyVerifiedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
