package com.gadget.rental.booking;

import org.hibernate.validator.constraints.UUID;

public record BookingByRequestReferenceDTO(
        @UUID String requestReferenceNumber) {
}
