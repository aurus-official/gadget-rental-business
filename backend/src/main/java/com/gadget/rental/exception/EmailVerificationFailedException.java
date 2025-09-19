package com.gadget.rental.exception;

public class EmailVerificationFailedException extends RuntimeException {
    public EmailVerificationFailedException(String message) {
        super(message);
    }

    public EmailVerificationFailedException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
