package com.gadget.rental.booking.client;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import jakarta.validation.Valid;

import com.gadget.rental.booking.BookingByUserEmailResponseDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/v1")
public class ClientBookingController {
    private final ClientBookingService clientBookingService;

    ClientBookingController(ClientBookingService clientBookingService) {
        this.clientBookingService = clientBookingService;
    }

    @PostMapping(path = "/client/bookings")
    ResponseEntity<String> createBookingByClient(@Valid @RequestPart("idPics") MultipartFile[] idPics,
            @RequestPart("validBookingDateFrom") String validBookingDateFrom,
            @RequestPart("validBookingDateUntil") String validBookingDateUntil,
            @RequestPart("productIds") String productIds) {

        @Valid
        ClientBookingDTO clientBookingDTO = new ClientBookingDTO(idPics, ZonedDateTime.parse(validBookingDateFrom),
                ZonedDateTime.parse(validBookingDateUntil.toString()),
                Arrays.stream(productIds.split(", ")).map((id) -> Long.valueOf(id)).toArray(Long[]::new));

        String referenceNumber = clientBookingService.createBookingToGetReferenceNumber(clientBookingDTO);
        return ResponseEntity.ok(referenceNumber);
    }

    @GetMapping(path = "/client/bookings")
    ResponseEntity<List<BookingByUserEmailResponseDTO>> getAllBookingsByUserEmail() {
        List<BookingByUserEmailResponseDTO> bookingByUserEmailResponseDTOs = clientBookingService
                .getAllBookingsByUserEmail();
        return ResponseEntity.ok(bookingByUserEmailResponseDTOs);
    }

    @DeleteMapping(path = "/client/{email}/bookings/{requestReferenceNumber}")
    ResponseEntity<String> cancelBookingByReferenceNumber(
            @PathVariable("requestReferenceNumber") String requestReferenceNumber,
            @PathVariable("email") String email) {
        String message = clientBookingService.cancelBookingByUserEmailAndRequestReferenceNumber(requestReferenceNumber);
        return ResponseEntity.ok(message);
    }

}
