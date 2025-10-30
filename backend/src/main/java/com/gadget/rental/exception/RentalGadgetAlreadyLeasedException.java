package com.gadget.rental.exception;

public class RentalGadgetAlreadyLeasedException extends RuntimeException {
    public RentalGadgetAlreadyLeasedException(String message) {
        super(message);
    }

    public RentalGadgetAlreadyLeasedException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
