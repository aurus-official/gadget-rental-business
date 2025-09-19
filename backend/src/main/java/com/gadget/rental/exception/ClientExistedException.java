package com.gadget.rental.exception;

public class ClientExistedException extends RuntimeException {

    public ClientExistedException(String message) {
        super(message);
    }

    public ClientExistedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
