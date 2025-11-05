package com.gadget.rental.booking.client;

import java.util.List;

import jakarta.validation.Valid;

import com.gadget.rental.booking.BookingByUserEmailResponseDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class ClientBookingController {
    private final ClientBookingService clientBookingService;

    ClientBookingController(ClientBookingService clientBookingService) {
        this.clientBookingService = clientBookingService;
    }

    @PostMapping(path = "/client/bookings")
    ResponseEntity<String> createBookingByClient(@Valid @RequestBody ClientBookingDTO clientBookingDTO) {
        String referenceNumber = clientBookingService.createBookingToGetReferenceNumber(clientBookingDTO);
        return ResponseEntity.ok(referenceNumber);
    }

    @GetMapping(path = "/client/bookings")
    ResponseEntity<List<BookingByUserEmailResponseDTO>> getAllBookingsByUserEmail() {
        List<BookingByUserEmailResponseDTO> bookingByUserEmailResponseDTOs = clientBookingService
                .getAllBookingsByUserEmail();
        return ResponseEntity.ok(bookingByUserEmailResponseDTOs);
    }

    @DeleteMapping(path = "/client/bookings/{requestReferenceNumber}")
    ResponseEntity<String> cancelBookingByReferenceNumber(
            @PathVariable("requestReferenceNumber") String requestReferenceNumber) {
        String message = clientBookingService.cancelBookingByUserEmailAndRequestReferenceNumber(requestReferenceNumber);
        return ResponseEntity.ok(message);
    }

}
