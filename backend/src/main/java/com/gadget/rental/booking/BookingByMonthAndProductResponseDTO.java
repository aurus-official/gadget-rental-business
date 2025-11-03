package com.gadget.rental.booking;

import java.time.ZonedDateTime;

public record BookingByMonthAndProductResponseDTO(
        ZonedDateTime validBookingDateFrom,
        ZonedDateTime validBookingDateUntil) {
}
