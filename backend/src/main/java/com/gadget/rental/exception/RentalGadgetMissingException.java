package com.gadget.rental.exception;

public class RentalGadgetMissingException extends RuntimeException {
    public RentalGadgetMissingException(String message) {
        super(message);
    }

    public RentalGadgetMissingException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
