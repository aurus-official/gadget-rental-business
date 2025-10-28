package com.gadget.rental.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class BookingController {

    @PostMapping(path = "/bookings")
    ResponseEntity<String> createBooking() {
        return ResponseEntity.ok("");
    }
}
