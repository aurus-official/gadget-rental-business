package com.gadget.rental.exception;

public class BookingExpiredException extends RuntimeException {
    public BookingExpiredException(String message) {
        super(message);
    }

    public BookingExpiredException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
