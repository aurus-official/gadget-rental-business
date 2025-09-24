package com.gadget.rental.exception;

public class ClientUsernameDuplicateException extends RuntimeException {
    public ClientUsernameDuplicateException(String message) {
        super(message);
    }

    public ClientUsernameDuplicateException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
