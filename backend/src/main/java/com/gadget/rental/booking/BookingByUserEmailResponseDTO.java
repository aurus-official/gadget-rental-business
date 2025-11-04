package com.gadget.rental.booking;

import java.time.ZonedDateTime;
import java.util.List;

public record BookingByUserEmailResponseDTO(
        ZonedDateTime validBookingDateFrom,
        ZonedDateTime validBookingDateUntil,
        String requestReferenceNumber,
        String createdBy,
        List<Long> productIds) {
}
