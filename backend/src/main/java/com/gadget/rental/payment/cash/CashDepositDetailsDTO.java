package com.gadget.rental.payment.cash;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;

public record CashDepositDetailsDTO(
        @NotEmpty @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "Email is invalid.") String email,
        @NotEmpty String requestReferenceNumber,
        @Max(value = 99999) double price) {
}
