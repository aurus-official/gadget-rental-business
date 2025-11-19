package com.gadget.rental.booking.client;

import java.time.ZonedDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

public record ClientBookingDTO(
        @Size(min = 1, max = 3) MultipartFile[] idPics,
        @NotNull ZonedDateTime validBookingDateFrom,
        @NotNull ZonedDateTime validBookingDateUntil,
        @NotEmpty Long[] productIds) {
}
