package com.gadget.rental.exception;

public class EmailVerificationResendTooSoonException extends RuntimeException {

    public EmailVerificationResendTooSoonException(String message) {
        super(message);
    }

    public EmailVerificationResendTooSoonException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
