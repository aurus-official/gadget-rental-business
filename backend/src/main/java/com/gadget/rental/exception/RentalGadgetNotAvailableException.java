package com.gadget.rental.exception;

public class RentalGadgetNotAvailableException extends RuntimeException {
    public RentalGadgetNotAvailableException(String message) {
        super(message);
    }

    public RentalGadgetNotAvailableException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
