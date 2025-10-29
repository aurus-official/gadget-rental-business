package com.gadget.rental.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class BookingController {

    private final BookingService bookingService;

    BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping(path = "/bookings")
    ResponseEntity<String> createBooking(@RequestBody BookingDTO bookingDTO) {
        String referenceNumber = bookingService.createBookingToGetReferenceNumber(bookingDTO);
        return ResponseEntity.ok(referenceNumber);
    }
}
