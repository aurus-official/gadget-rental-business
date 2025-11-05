package com.gadget.rental.exception;

public class InvalidPaymentTransactionSequenceException extends RuntimeException {
    public InvalidPaymentTransactionSequenceException(String message) {
        super(message);
    }

    public InvalidPaymentTransactionSequenceException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
