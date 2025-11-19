package com.gadget.rental.booking.admin;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import jakarta.validation.Valid;

import com.gadget.rental.booking.BookingByRequestReferenceDTO;
import com.gadget.rental.booking.BookingByUserEmailRequestDTO;
import com.gadget.rental.booking.BookingByUserEmailResponseDTO;
import com.gadget.rental.booking.BookingStatus;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/v1")
public class AdminBookingController {

    private final AdminBookingService adminBookingService;

    AdminBookingController(AdminBookingService adminBookingService) {
        this.adminBookingService = adminBookingService;
    }

    @PostMapping(path = "/admin/bookings")
    ResponseEntity<String> createBookingByAdmin(@RequestPart("idPics") MultipartFile[] idPics,
            @RequestPart("validBookingDateFrom") String validBookingDateFrom,
            @RequestPart("validBookingDateUntil") String validBookingDateUntil,
            @RequestPart("clientEmail") String clientEmail,
            @RequestPart("productIds") String productIds) {

        @Valid
        AdminBookingDTO adminBookingDTO = new AdminBookingDTO(idPics, ZonedDateTime.parse(validBookingDateFrom),
                ZonedDateTime.parse(validBookingDateUntil.toString()),
                clientEmail,
                Arrays.stream(productIds.split(", ")).map((id) -> Long.valueOf(id)).toArray(Long[]::new));
        String referenceNumber = adminBookingService.createBookingToGetReferenceNumber(adminBookingDTO);
        return ResponseEntity.ok(referenceNumber);
    }

    @GetMapping(path = "/admin/bookings/clients/{email}")
    ResponseEntity<List<BookingByUserEmailResponseDTO>> getAllBookingsByUserEmail(
            @Valid BookingByUserEmailRequestDTO bookingByUserEmailRequestDTO) {
        List<BookingByUserEmailResponseDTO> bookingByUserEmailResponseDTOs = adminBookingService
                .getAllBookingsByUserEmail(bookingByUserEmailRequestDTO.email());
        return ResponseEntity.ok(bookingByUserEmailResponseDTOs);
    }

    @PutMapping(path = "/admin/bookings/{requestReferenceNumber}/clients/{email}")
    ResponseEntity<String> updateBookingStatusByUserEmailAndRequestReferenceNumber(
            @PathVariable("requestReferenceNumber") String requestReferenceNumber,
            @RequestParam("updateStatus") String status) {

        @Valid
        BookingByRequestReferenceDTO bookingByRequestReferenceDTO = new BookingByRequestReferenceDTO(
                requestReferenceNumber);

        String message = "";

        BookingStatus bookingStatus = BookingStatus.valueOf(status.toLowerCase());
        switch (bookingStatus) {
            case ONGOING:
                message = adminBookingService
                        .activeLeaseBookingByUserEmailAndRequestReferenceNumber(
                                bookingByRequestReferenceDTO);
                break;
            case COMPLETED:
                message = adminBookingService
                        .closeBookingByUserEmailAndRequestReferenceNumber(bookingByRequestReferenceDTO);
                break;
            case CANCELLED:
                message = adminBookingService
                        .cancelBookingByUserEmailAndRequestReferenceNumber(bookingByRequestReferenceDTO);
                break;
            default:
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Nothing happened.");
        }

        return ResponseEntity.ok(message);
    }
}
