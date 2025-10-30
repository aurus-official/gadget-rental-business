package com.gadget.rental.booking;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.gadget.rental.auth.jwt.JwtAuthenticationToken;
import com.gadget.rental.exception.BookingConflictException;
import com.gadget.rental.exception.RentalGadgetAlreadyLeasedException;
import com.gadget.rental.exception.RentalGadgetMissingException;
import com.gadget.rental.rental.RentalGadgetModel;
import com.gadget.rental.rental.RentalGadgetRepository;
import com.gadget.rental.rental.RentalGadgetStatus;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RentalGadgetRepository rentalGadgetRepository;

    public BookingService(BookingRepository bookingRepository, RentalGadgetRepository rentalGadgetRepository) {
        this.bookingRepository = bookingRepository;
        this.rentalGadgetRepository = rentalGadgetRepository;
    }

    public String createBookingByClientToGetReferenceNumber(ClientCreatedBookingDTO bookingDTO) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) securityContext.getAuthentication();

        if (isBookingOverlapping(bookingDTO.validBookingDateFrom(), bookingDTO.validBookingDateUntil())) {
            throw new BookingConflictException(
                    String.format("The requested booking time overlaps with an existing booking."));
        }

        for (long id : bookingDTO.productIds()) {
            RentalGadgetModel rentalGadget = rentalGadgetRepository.findById(id)
                    .orElseThrow(() -> new RentalGadgetMissingException("Rental gadget listing is missing."));

            if (rentalGadget.getRentalGadgetStatus() != RentalGadgetStatus.AVAILABLE) {
                throw new RentalGadgetAlreadyLeasedException(
                        String.format("Rental gadget listing %s is leased.", rentalGadget.getName()));
            }
        }

        BookingModel booking = new BookingModel();
        booking.setCreatedBy(jwtAuthenticationToken.getName());
        booking.setCreatedFor(jwtAuthenticationToken.getName());
        booking.setValidBookingDateFrom(bookingDTO.validBookingDateFrom());
        booking.setValidBookingDateUntil(bookingDTO.validBookingDateUntil());
        booking.setValidConfirmationDateFrom(ZonedDateTime.now(ZoneId.of("Z")));
        booking.setValidConfirmationDateUntil(booking.getValidConfirmationDateFrom().plusHours(12));
        booking.setReferenceNumber(UUID.randomUUID().toString());

        bookingRepository.save(booking);

        return booking.getReferenceNumber();
    }

    public String createBookingByAdminToGetReferenceNumber(AdminCreatedBookingDTO bookingDTO) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) securityContext.getAuthentication();

        if (isBookingOverlapping(bookingDTO.validBookingDateFrom(), bookingDTO.validBookingDateUntil())) {
            throw new BookingConflictException(
                    String.format("The requested booking time overlaps with an existing booking."));
        }

        for (long id : bookingDTO.productIds()) {
            RentalGadgetModel rentalGadget = rentalGadgetRepository.findById(id)
                    .orElseThrow(() -> new RentalGadgetMissingException("Rental gadget listing is missing."));

            if (rentalGadget.getRentalGadgetStatus() != RentalGadgetStatus.AVAILABLE) {
                throw new RentalGadgetAlreadyLeasedException(
                        String.format("Rental gadget listing %s is leased.", rentalGadget.getName()));
            }
        }

        BookingModel booking = new BookingModel();
        booking.setCreatedBy(jwtAuthenticationToken.getName());
        booking.setCreatedFor(bookingDTO.clientEmail());
        booking.setValidBookingDateFrom(bookingDTO.validBookingDateFrom());
        booking.setValidBookingDateUntil(bookingDTO.validBookingDateUntil());
        booking.setValidConfirmationDateFrom(ZonedDateTime.now(ZoneId.of("Z")));
        booking.setValidConfirmationDateUntil(booking.getValidConfirmationDateFrom().plusHours(12));
        booking.setReferenceNumber(UUID.randomUUID().toString());

        bookingRepository.save(booking);

        return booking.getReferenceNumber();
    }

    private boolean isBookingOverlapping(ZonedDateTime validBookingDateFrom, ZonedDateTime validBookingDateUntil) {
        List<BookingModel> allValidBookingList = bookingRepository
                .findAllValidBookings(validBookingDateFrom, validBookingDateUntil);
        if (allValidBookingList.isEmpty()) {
            return false;
        }
        return true;
    }

}
