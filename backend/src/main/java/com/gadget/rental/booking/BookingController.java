package com.gadget.rental.booking;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class BookingController {

    private final BookingService bookingService;

    BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping(path = "/bookings/{pId}/{month}/{year}")
    ResponseEntity<List<BookingByMonthAndProductResponseDTO>> getAllBookingsByMonthAndProduct(
            @Valid BookingByMonthAndProductRequestDTO bookingByMonthAndProductRequestDTO) {
        List<BookingByMonthAndProductResponseDTO> bookingByMonthAndProductResponseDTOs = bookingService
                .getAllBookingsByMonthAndProduct(bookingByMonthAndProductRequestDTO);
        return ResponseEntity.ok(bookingByMonthAndProductResponseDTOs);
    }
}
