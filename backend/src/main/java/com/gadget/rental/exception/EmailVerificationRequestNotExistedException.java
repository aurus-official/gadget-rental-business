package com.gadget.rental.exception;

public class EmailVerificationRequestNotExistedException extends RuntimeException {
    public EmailVerificationRequestNotExistedException(String message) {
        super(message);
    }

    public EmailVerificationRequestNotExistedException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
