package com.gadget.rental.booking;

import java.time.ZonedDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record AdminCreatedBookingDTO(
        @NotEmpty ZonedDateTime validBookingDateFrom,
        @NotEmpty ZonedDateTime validBookingDateUntil,
        @NotEmpty @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "Email is invalid.") String clientEmail,
        @NotEmpty long[] productIds) {
}
