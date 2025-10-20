package com.gadget.rental.rental;

import java.time.ZonedDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

public record RentalGadgetDTO(
        @Size(min = 1, max = 3) MultipartFile[] images,
        @NotEmpty String name,
        @NotEmpty ZonedDateTime createdAt,
        @NotEmpty @Max(value = 2000) String description) {
}
