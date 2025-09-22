package com.gadget.rental.exception;

public class InvalidEmailVerificationCodeException extends RuntimeException {
    public InvalidEmailVerificationCodeException(String message) {
        super(message);
    }

    public InvalidEmailVerificationCodeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
