package com.gadget.rental.exception;

public class RentalGadgetImageExistedException extends RuntimeException {
    public RentalGadgetImageExistedException(String message) {
        super(message);
    }

    public RentalGadgetImageExistedException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
