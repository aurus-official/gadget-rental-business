package com.gadget.rental.exception;

public class RentalGadgetExistedException extends RuntimeException {
    public RentalGadgetExistedException(String message) {
        super(message);
    }

    public RentalGadgetExistedException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
