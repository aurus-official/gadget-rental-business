package com.gadget.rental.payment.online;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record OnlinePaymentDetailsDTO(
        @NotEmpty String firstName,
        @NotEmpty String lastName,
        @NotEmpty @Pattern(regexp = "^(\\+63|0)9\\d{9}$", message = "Phone number is invalid.") String phoneNumber,
        @NotEmpty String requestReferenceNumber) {
}
