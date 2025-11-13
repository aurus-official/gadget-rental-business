package com.gadget.rental.exception;

public class PaymentTransactionInProgressException extends RuntimeException {
    public PaymentTransactionInProgressException(String message) {
        super(message);
    }

    public PaymentTransactionInProgressException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
