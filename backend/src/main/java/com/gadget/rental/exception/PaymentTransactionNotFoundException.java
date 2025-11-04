package com.gadget.rental.exception;

public class PaymentTransactionNotFoundException extends RuntimeException {
    public PaymentTransactionNotFoundException(String message) {
        super(message);
    }

    public PaymentTransactionNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
