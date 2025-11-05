package com.gadget.rental.booking.admin;

import java.util.List;

import jakarta.validation.Valid;

import com.gadget.rental.booking.BookingByUserEmailAndRequestReferenceDTO;
import com.gadget.rental.booking.BookingByUserEmailRequestDTO;
import com.gadget.rental.booking.BookingByUserEmailResponseDTO;
import com.gadget.rental.booking.BookingStatus;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class AdminBookingController {

    private final AdminBookingService adminBookingService;

    AdminBookingController(AdminBookingService adminBookingService) {
        this.adminBookingService = adminBookingService;
    }

    @PostMapping(path = "/admin/bookings")
    ResponseEntity<String> createBookingByAdmin(@Valid @RequestBody AdminBookingDTO adminBookingDTO) {
        String referenceNumber = adminBookingService.createBookingToGetReferenceNumber(adminBookingDTO);
        return ResponseEntity.ok(referenceNumber);
    }

    @GetMapping(path = "/admin/bookings/client/{email}")
    ResponseEntity<List<BookingByUserEmailResponseDTO>> getAllBookingsByUserEmail(
            @Valid BookingByUserEmailRequestDTO bookingByUserEmailRequestDTO) {
        List<BookingByUserEmailResponseDTO> bookingByUserEmailResponseDTOs = adminBookingService
                .getAllBookingsByUserEmail(bookingByUserEmailRequestDTO.email());
        return ResponseEntity.ok(bookingByUserEmailResponseDTOs);
    }

    @PutMapping(path = "/admin/bookings/{requestReferenceNumber}/client/{email}")
    ResponseEntity<String> updateBookingStatusByUserEmailAndRequestReferenceNumber(
            @PathVariable("requestReferenceNumber") String requestReferenceNumber, @PathVariable("email") String email,
            @RequestParam("updateStatus") BookingStatus status) {

        @Valid
        BookingByUserEmailAndRequestReferenceDTO bookingByUserEmailAndRequestReferenceDTO = new BookingByUserEmailAndRequestReferenceDTO(
                requestReferenceNumber, email);

        String message = "";

        switch (status) {
            case ONGOING:
                message = adminBookingService
                        .activeLeaseBookingByUserEmailAndRequestReferenceNumber(
                                bookingByUserEmailAndRequestReferenceDTO);
                break;
            case COMPLETED:
                message = adminBookingService
                        .closeBookingByUserEmailAndRequestReferenceNumber(bookingByUserEmailAndRequestReferenceDTO);
                break;
            case CANCELLED:
                message = adminBookingService
                        .cancelBookingByUserEmailAndRequestReferenceNumber(bookingByUserEmailAndRequestReferenceDTO);
                break;
            default:
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Nothing happened.");
        }

        return ResponseEntity.ok(message);
    }
}
