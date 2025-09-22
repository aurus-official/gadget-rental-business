package com.gadget.rental.exception;

public class EmailVerificationInProgressException extends RuntimeException {
    public EmailVerificationInProgressException(String message) {
        super(message);
    }

    public EmailVerificationInProgressException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
