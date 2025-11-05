package com.gadget.rental.booking.client;

import java.time.ZonedDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ClientBookingDTO(
        @NotNull ZonedDateTime validBookingDateFrom,
        @NotNull ZonedDateTime validBookingDateUntil,
        @NotEmpty long[] productIds) {
}
