package com.gadget.rental.exception;

public class ClientAccountExistedException extends RuntimeException {

    public ClientAccountExistedException(String message) {
        super(message);
    }

    public ClientAccountExistedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
