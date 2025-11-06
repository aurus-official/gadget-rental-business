package com.gadget.rental.payment.cash;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.UUID;

public record CashPaymentTransactionRequestDTO(
        @UUID String checkoutId,
        @UUID String requestReferenceNumber,
        @NotEmpty @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "Email is invalid.") String email) {
}
