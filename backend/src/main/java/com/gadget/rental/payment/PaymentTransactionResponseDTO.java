package com.gadget.rental.payment;

public record PaymentTransactionResponseDTO(
        String paymentScheme,
        double totalPrice,
        String requestReferenceNumber,
        PaymentStatus status,
        String email,
        String createdBy,
        String currency) {
}
