package com.gadget.rental.booking.admin;

import java.time.ZonedDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AdminBookingDTO(
        @NotNull ZonedDateTime validBookingDateFrom,
        @NotNull ZonedDateTime validBookingDateUntil,
        @NotEmpty @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "Email is invalid.") String clientEmail,
        @NotEmpty long[] productIds) {
}
