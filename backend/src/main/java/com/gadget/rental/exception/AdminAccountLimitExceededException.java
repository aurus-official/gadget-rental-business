package com.gadget.rental.exception;

public class AdminAccountLimitExceededException extends RuntimeException {
    public AdminAccountLimitExceededException(String message) {
        super(message);
    }

    public AdminAccountLimitExceededException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
