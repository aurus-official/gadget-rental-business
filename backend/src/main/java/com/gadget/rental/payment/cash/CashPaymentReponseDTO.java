package com.gadget.rental.payment.cash;

public record CashPaymentReponseDTO(
        String message,
        String requestReferenceNumber,
        String checkoutId) {
}
