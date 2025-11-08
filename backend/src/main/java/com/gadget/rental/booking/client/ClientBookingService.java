package com.gadget.rental.booking.client;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.gadget.rental.auth.jwt.JwtAuthenticationToken;
import com.gadget.rental.booking.BookingByUserEmailResponseDTO;
import com.gadget.rental.booking.BookingModel;
import com.gadget.rental.booking.BookingRepository;
import com.gadget.rental.booking.BookingStatus;
import com.gadget.rental.exception.BookingConflictException;
import com.gadget.rental.exception.BookingNotFoundException;
import com.gadget.rental.exception.RentalGadgetNotAvailableException;
import com.gadget.rental.exception.RentalGadgetNotFoundException;
import com.gadget.rental.rental.RentalGadgetModel;
import com.gadget.rental.rental.RentalGadgetRepository;
import com.gadget.rental.rental.RentalGadgetStatus;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.lang.Collections;

@Service
public class ClientBookingService {
    private final BookingRepository bookingRepository;
    private final RentalGadgetRepository rentalGadgetRepository;

    public ClientBookingService(BookingRepository bookingRepository, RentalGadgetRepository rentalGadgetRepository) {
        this.bookingRepository = bookingRepository;
        this.rentalGadgetRepository = rentalGadgetRepository;
    }

    public String createBookingToGetReferenceNumber(ClientBookingDTO bookingDTO) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) securityContext.getAuthentication();

        if (isBookingOverlapping(bookingDTO.validBookingDateFrom(), bookingDTO.validBookingDateUntil())) {
            throw new BookingConflictException(
                    String.format("The requested booking time overlaps with an existing booking."));
        }

        if (bookingDTO.productIds().length == 0) {
            throw new RentalGadgetNotFoundException("Rental product id is blank.");
        }

        BookingModel booking = new BookingModel();

        for (long id : bookingDTO.productIds()) {
            RentalGadgetModel rentalGadget = rentalGadgetRepository.findById(id)
                    .orElseThrow(() -> new RentalGadgetNotFoundException("Rental gadget listing is missing."));

            if (rentalGadget.getStatus() == RentalGadgetStatus.AVAILABLE) {
                booking.addRentalGadgetProductId(id);
                rentalGadget.setStatus(RentalGadgetStatus.NOT_AVAILABLE);
                continue;
            }

            throw new RentalGadgetNotAvailableException(
                    String.format("Rental gadget listing %s is not available.", rentalGadget.getName()));
        }

        booking.setCreatedBy(jwtAuthenticationToken.getName());
        booking.setCreatedFor(jwtAuthenticationToken.getName());
        booking.setValidBookingDateFrom(bookingDTO.validBookingDateFrom());
        booking.setValidBookingDateUntil(bookingDTO.validBookingDateUntil());
        booking.setValidConfirmationDateFrom(ZonedDateTime.now(ZoneId.of("Z")));
        booking.setValidConfirmationDateUntil(booking.getValidConfirmationDateFrom().plusHours(12));
        booking.setRequestReferenceNumber(UUID.randomUUID().toString());
        booking.setStatus(BookingStatus.PENDING);
        bookingRepository.save(booking);

        return booking.getRequestReferenceNumber();
    }

    public List<BookingByUserEmailResponseDTO> getAllBookingsByUserEmail() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) securityContext.getAuthentication();

        String email = jwtAuthenticationToken.getName();

        List<BookingModel> bookings = bookingRepository.findAllValidBookingsCreatedForClient(email);

        if (bookings.isEmpty()) {
            return Collections.emptyList();
        }

        List<BookingByUserEmailResponseDTO> byUserEmailResponseDTOs = new ArrayList<>();

        for (BookingModel booking : bookings) {
            BookingByUserEmailResponseDTO byUserEmailResponseDTO = new BookingByUserEmailResponseDTO(
                    booking.getValidBookingDateFrom(), booking.getValidBookingDateUntil(),
                    booking.getRequestReferenceNumber(), booking.getCreatedBy(),
                    booking.getRentalGadgetProductIdList());

            byUserEmailResponseDTOs.add(byUserEmailResponseDTO);
        }

        return byUserEmailResponseDTOs;
    }

    public String cancelBookingByUserEmailAndRequestReferenceNumber(String requestReferenceNumber) {
        BookingModel booking = bookingRepository
                .findBookingByRequestReferenceNumber(requestReferenceNumber)
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Payment booking with reference number '%s' not found.",
                                requestReferenceNumber)));

        for (long productId : booking.getRentalGadgetProductIdList()) {
            RentalGadgetModel rentalGadget = rentalGadgetRepository.findById(productId)
                    .orElseThrow(() -> new RentalGadgetNotFoundException("Rental gadget listing is missing."));
            rentalGadget.setStatus(RentalGadgetStatus.AVAILABLE);
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            String message = String.format(
                    "This booking has already been paid for. Please contact the admin to request cancellation of this transaction.");
            return message;
        }

        String message = String.format("Booking with reference number '%s' was cancelled.", requestReferenceNumber);
        booking.setStatus(BookingStatus.CANCELLED);
        return message;
    }

    private boolean isBookingOverlapping(ZonedDateTime validBookingDateFrom, ZonedDateTime validBookingDateUntil) {
        List<BookingModel> allValidBookingList = bookingRepository
                .findAllValidBookingsByDuration(validBookingDateFrom, validBookingDateUntil);
        if (allValidBookingList.isEmpty()) {
            return false;
        }

        return true;
    }
}
