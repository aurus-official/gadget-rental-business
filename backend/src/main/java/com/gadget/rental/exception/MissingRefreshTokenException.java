package com.gadget.rental.exception;

public class MissingRefreshTokenException extends RuntimeException {
    public MissingRefreshTokenException(String message) {
        super(message);
    }

    public MissingRefreshTokenException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
