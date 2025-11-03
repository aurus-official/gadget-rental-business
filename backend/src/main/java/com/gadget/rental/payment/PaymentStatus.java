package com.gadget.rental.payment;

public enum PaymentStatus {
    PAYMENT_PENDING("payment_pending"), PAYMENT_SUCCESS("payment_success"), PAYMENT_FAILED("payment_failed"),
    PAYMENT_EXPIRED("payment_expired"), PAYMENT_CANCELLED("payment_cancelled"), AUTHORIZED("authorized"),
    AUTH_FAILED("auth_failed");

    String value;

    PaymentStatus(String value) {
        this.value = value;
    }
}
