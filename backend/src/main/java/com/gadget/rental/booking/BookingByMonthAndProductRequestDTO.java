package com.gadget.rental.booking;

import java.time.Month;

import org.springframework.web.bind.annotation.RequestParam;

public record BookingByMonthAndProductRequestDTO(
        @RequestParam("pId") long pId,
        @RequestParam("month") Month month,
        @RequestParam("year") int year) {
}
