package com.gadget.rental.exception;

public class EmailVerificationAttemptLimitReachedException extends RuntimeException {
    public EmailVerificationAttemptLimitReachedException(String message) {
        super(message);
    }

    public EmailVerificationAttemptLimitReachedException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
