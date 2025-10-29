package com.gadget.rental.booking;

import java.time.ZonedDateTime;

public record BookingDTO(
        ZonedDateTime validBookingDateFrom,
        ZonedDateTime validBookingDateUntil,
        int[] productIds) {
}
