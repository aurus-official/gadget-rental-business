package com.gadget.rental.payment.cash;

import org.hibernate.validator.constraints.UUID;

public record CashTransactionRequestDTO(
        @UUID String checkoutId,
        @UUID String requestReferenceNumber) {
}
