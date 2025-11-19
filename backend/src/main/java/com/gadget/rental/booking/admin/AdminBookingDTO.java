package com.gadget.rental.booking.admin;

import java.time.ZonedDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

public record AdminBookingDTO(
        @Size(min = 1, max = 3) MultipartFile[] idPics,
        @NotNull ZonedDateTime validBookingDateFrom,
        @NotNull ZonedDateTime validBookingDateUntil,
        @NotEmpty @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "Email is invalid.") String clientEmail,
        @NotEmpty Long[] productIds) {
}
