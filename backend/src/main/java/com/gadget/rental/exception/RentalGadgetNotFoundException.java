package com.gadget.rental.exception;

public class RentalGadgetNotFoundException extends RuntimeException {
    public RentalGadgetNotFoundException(String message) {
        super(message);
    }

    public RentalGadgetNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
