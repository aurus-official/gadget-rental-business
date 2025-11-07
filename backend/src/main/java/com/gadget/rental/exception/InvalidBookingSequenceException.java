package com.gadget.rental.exception;

public class InvalidBookingSequenceException extends RuntimeException {
    public InvalidBookingSequenceException(String message) {
        super(message);
    }

    public InvalidBookingSequenceException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
