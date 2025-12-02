package com.gadget.rental.exception;

public class AccountCreationTokenMismatchException extends RuntimeException {
    public AccountCreationTokenMismatchException(String message) {
        super(message);
    }

    public AccountCreationTokenMismatchException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
