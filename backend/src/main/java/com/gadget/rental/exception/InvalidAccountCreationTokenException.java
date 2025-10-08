package com.gadget.rental.exception;

public class InvalidAccountCreationTokenException extends RuntimeException {
    public InvalidAccountCreationTokenException(String message) {
        super(message);
    }

    public InvalidAccountCreationTokenException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
