package com.gadget.rental.booking;

import java.time.ZonedDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ClientCreatedBookingDTO(
        @NotNull ZonedDateTime validBookingDateFrom,
        @NotNull ZonedDateTime validBookingDateUntil,
        @NotEmpty long[] productIds) {
}
