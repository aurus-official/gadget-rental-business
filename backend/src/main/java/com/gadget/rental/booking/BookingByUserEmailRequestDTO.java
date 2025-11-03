package com.gadget.rental.booking;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import org.springframework.web.bind.annotation.PathVariable;

public record BookingByUserEmailRequestDTO(
        @PathVariable("email") @NotEmpty @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "Email is invalid.") String email) {
}
