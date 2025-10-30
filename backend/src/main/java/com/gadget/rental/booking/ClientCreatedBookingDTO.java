package com.gadget.rental.booking;

import java.time.ZonedDateTime;

import jakarta.validation.constraints.NotEmpty;

public record ClientCreatedBookingDTO(
        @NotEmpty ZonedDateTime validBookingDateFrom,
        @NotEmpty ZonedDateTime validBookingDateUntil,
        @NotEmpty long[] productIds) {
}
