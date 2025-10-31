package com.gadget.rental.exception;

public class EmailVerificationRequestNotFoundException extends RuntimeException {
    public EmailVerificationRequestNotFoundException(String message) {
        super(message);
    }

    public EmailVerificationRequestNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
