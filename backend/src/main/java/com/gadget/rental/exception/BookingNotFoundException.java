package com.gadget.rental.exception;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String message) {
        super(message);
    }

    public BookingNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
