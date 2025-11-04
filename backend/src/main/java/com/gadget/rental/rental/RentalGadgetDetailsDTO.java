package com.gadget.rental.rental;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RentalGadgetDetailsDTO(
        @NotEmpty String name,
        @Max(value = 99999) double price,
        @NotEmpty @Size(min = 10, max = 500) String description) {
}
