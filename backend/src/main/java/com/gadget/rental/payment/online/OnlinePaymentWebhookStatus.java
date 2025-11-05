package com.gadget.rental.payment.online;

public enum OnlinePaymentWebhookStatus {
    PAYMENT_PENDING("payment_pending"), PAYMENT_SUCCESS("payment_success"), PAYMENT_FAILED("payment_failed"),
    PAYMENT_EXPIRED("payment_expired"), PAYMENT_CANCELLED("payment_cancelled"), AUTHORIZED("authorized"),
    CAPTURED("captured"), AUTH_FAILED("auth_failed");

    String value;

    OnlinePaymentWebhookStatus(String value) {
        this.value = value;
    }
}
