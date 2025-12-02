package com.gadget.rental.exception;

public class MissingAccessTokenException extends RuntimeException {
    public MissingAccessTokenException(String message) {
        super(message);
    }

    public MissingAccessTokenException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
