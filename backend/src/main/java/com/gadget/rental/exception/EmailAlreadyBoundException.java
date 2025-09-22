package com.gadget.rental.exception;

public class EmailAlreadyBoundException extends RuntimeException {
    public EmailAlreadyBoundException(String message) {
        super(message);
    }

    public EmailAlreadyBoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
