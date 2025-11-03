package com.gadget.rental.booking;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/v1")
public class BookingController {

    private final BookingService bookingService;

    BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping(path = "/bookings/client")
    ResponseEntity<String> createBookingByClient(@RequestBody ClientCreatedBookingDTO bookingDTO) {
        String referenceNumber = bookingService.createBookingByClientToGetReferenceNumber(bookingDTO);
        return ResponseEntity.ok(referenceNumber);
    }

    @PostMapping(path = "/bookings/admin")
    ResponseEntity<String> createBookingByAdmin(@RequestBody AdminCreatedBookingDTO bookingDTO) {
        String referenceNumber = bookingService.createBookingByAdminToGetReferenceNumber(bookingDTO);
        return ResponseEntity.ok(referenceNumber);
    }

    @GetMapping(path = "/bookings/{pId}/{month}/{year}")
    ResponseEntity<List<BookingByMonthAndProductResponseDTO>> getAllBookingsByMonthAndProduct(
            BookingByMonthAndProductRequestDTO bookingByMonthAndProductRequestDTO) {
        List<BookingByMonthAndProductResponseDTO> bookingByMonthAndProductResponseDTOs = bookingService
                .getAllBookingsByMonthAndProduct(bookingByMonthAndProductRequestDTO);
        return ResponseEntity.ok(bookingByMonthAndProductResponseDTOs);
    }

    @GetMapping(path = "/bookings/users/{email}")
    ResponseEntity<List<BookingByUserEmailResponseDTO>> getAllBookingsByUserEmail(
            @Valid BookingByUserEmailRequestDTO bookingByUserEmailRequestDTO) {
        List<BookingByUserEmailResponseDTO> bookingByUserEmailResponseDTOs = bookingService
                .getAllBookingsByUserEmail(bookingByUserEmailRequestDTO.email());
        return ResponseEntity.ok(bookingByUserEmailResponseDTOs);
    }
}
