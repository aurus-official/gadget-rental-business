package com.gadget.rental.payment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.UUID;

public record PaymentTransactionRequestDTO(
        @UUID String requestTransactionNumber,
        @NotEmpty @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$") String email) {
}
