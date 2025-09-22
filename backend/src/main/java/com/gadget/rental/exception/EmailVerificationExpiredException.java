package com.gadget.rental.exception;

public class EmailVerificationExpiredException extends RuntimeException {
    public EmailVerificationExpiredException(String message) {
        super(message);
    }

    public EmailVerificationExpiredException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
