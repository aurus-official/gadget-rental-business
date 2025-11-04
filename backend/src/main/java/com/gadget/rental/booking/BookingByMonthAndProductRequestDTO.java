package com.gadget.rental.booking;

import java.time.Month;

import jakarta.validation.constraints.NotEmpty;

import org.springframework.web.bind.annotation.RequestParam;

public record BookingByMonthAndProductRequestDTO(
        @NotEmpty @RequestParam("pId") long pId,
        @NotEmpty @RequestParam("month") Month month,
        @NotEmpty @RequestParam("year") int year) {
}
