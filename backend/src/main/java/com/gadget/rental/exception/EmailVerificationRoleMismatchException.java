package com.gadget.rental.exception;

public class EmailVerificationRoleMismatchException extends RuntimeException {
    public EmailVerificationRoleMismatchException(String message) {
        super(message);
    }

    public EmailVerificationRoleMismatchException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
