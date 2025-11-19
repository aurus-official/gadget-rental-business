package com.gadget.rental.exception;

public class BookingAlreadyApprovedException extends RuntimeException {
    public BookingAlreadyApprovedException(String message) {
        super(message);
    }

    public BookingAlreadyApprovedException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
