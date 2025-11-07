package com.gadget.rental.payment;

public enum PaymentStatus {
    CASH_PAYMENT_PENDING("cash_payment_pending"), CASH_PAYMENT_CONFIRMED("cash_payment_confirmed"),
    CASH_PAYMENT_CANCELLED("cash_payment_cancelled"), CASH_DEPOSIT_PENDING("cash_deposit_pending"),
    CASH_DEPOSIT_CONFIRMED("cash_deposit_confirmed"), CASH_DEPOSIT_CAPTURED("cash_deposit_captured"),
    CASH_DEPOSIT_VOIDED("cash_deposit_voided"), CASH_DEPOSIT_CANCELLED("cash_deposit_cancelled"),

    ONLINE_PAYMENT_PENDING("online_payment_pending"), ONLINE_PAYMENT_CONFIRMED("online_payment_confirmed"),
    ONLINE_PAYMENT_CANCELLED("online_payment_cancelled"), ONLINE_PREAUTH_PENDING("online_preauth_pending"),
    ONLINE_PREAUTH_CONFIRMED("online_preauth_confirmed"), ONLINE_PREAUTH_CAPTURED("online_preauth_captured"),
    ONLINE_PREAUTH_VOIDED("online_preauth_voided"), ONLINE_PREAUTH_CANCELLED("online_preauth_cancelled");

    String value;

    PaymentStatus(String value) {
        this.value = value;
    }
}
