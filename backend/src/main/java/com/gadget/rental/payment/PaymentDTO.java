package com.gadget.rental.payment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record PaymentDTO(
        @NotEmpty String firstName,
        @NotEmpty String lastName,
        @NotEmpty @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "Email is invalid.") String email,
        @NotEmpty @Pattern(regexp = "^(\\+63|0)9\\d{9}$", message = "Phone number is invalid.") String phoneNumber,
        @NotEmpty String requestReferenceNumber) {
}
