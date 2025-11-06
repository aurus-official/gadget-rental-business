package com.gadget.rental.payment;

import java.math.BigDecimal;

public record PaymentTransactionHistoryResponseDTO(
        String paymentScheme,
        BigDecimal totalPrice,
        String requestReferenceNumber,
        String checkoutId,
        PaymentStatus status,
        String email,
        String createdBy,
        String currency) {
}
