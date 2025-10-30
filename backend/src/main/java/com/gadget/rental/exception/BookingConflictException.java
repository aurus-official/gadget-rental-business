package com.gadget.rental.exception;

public class BookingConflictException extends RuntimeException {
    public BookingConflictException(String message) {
        super(message);
    }

    public BookingConflictException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
