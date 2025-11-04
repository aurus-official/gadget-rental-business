package com.gadget.rental.booking;

import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.gadget.rental.auth.jwt.JwtAuthenticationToken;
import com.gadget.rental.exception.BookingConflictException;
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

        if (bookingDTO.productIds().length == 0) {
            throw new RentalGadgetNotFoundException("Rental product id is blank.");
        }

        for (long id : bookingDTO.productIds()) {
            RentalGadgetModel rentalGadget = rentalGadgetRepository.findById(id)
                    .orElseThrow(() -> new RentalGadgetNotFoundException("Rental gadget listing is missing."));

            if (rentalGadget.getStatus() == RentalGadgetStatus.AVAILABLE) {
                rentalGadget.setStatus(RentalGadgetStatus.BOOKED_UNPAID);
                continue;
            }

            throw new RentalGadgetNotAvailableException(
                    String.format("Rental gadget listing %s is not available.", rentalGadget.getName()));
        }

        BookingModel booking = new BookingModel();
        booking.setCreatedBy(jwtAuthenticationToken.getName());
        booking.setCreatedFor(jwtAuthenticationToken.getName());
        booking.setValidBookingDateFrom(bookingDTO.validBookingDateFrom());
        booking.setValidBookingDateUntil(bookingDTO.validBookingDateUntil());
        booking.setValidConfirmationDateFrom(ZonedDateTime.now(ZoneId.of("Z")));
        booking.setValidConfirmationDateUntil(booking.getValidConfirmationDateFrom().plusHours(12));
        booking.setRequestReferenceNumber(UUID.randomUUID().toString());

        for (long id : bookingDTO.productIds()) {
            booking.addRentalGadgetProductId(id);
        }

        bookingRepository.save(booking);

        return booking.getRequestReferenceNumber();
    }

    public String createBookingByAdminToGetReferenceNumber(AdminCreatedBookingDTO bookingDTO) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) securityContext.getAuthentication();

        if (isBookingOverlapping(bookingDTO.validBookingDateFrom(), bookingDTO.validBookingDateUntil())) {
            throw new BookingConflictException(
                    String.format("The requested booking time overlaps with an existing booking."));
        }

        if (bookingDTO.productIds().length == 0) {
            throw new RentalGadgetNotFoundException("Rental product id is blank.");
        }

        for (long id : bookingDTO.productIds()) {
            RentalGadgetModel rentalGadget = rentalGadgetRepository.findById(id)
                    .orElseThrow(() -> new RentalGadgetNotFoundException("Rental gadget listing is missing."));

            if (rentalGadget.getStatus() == RentalGadgetStatus.AVAILABLE) {
                rentalGadget.setStatus(RentalGadgetStatus.BOOKED_UNPAID);
                continue;
            }

            throw new RentalGadgetNotAvailableException(
                    String.format("Rental gadget listing %s is not available.", rentalGadget.getName()));
        }

        BookingModel booking = new BookingModel();
        booking.setCreatedBy(jwtAuthenticationToken.getName());
        booking.setCreatedFor(bookingDTO.clientEmail());
        booking.setValidBookingDateFrom(bookingDTO.validBookingDateFrom());
        booking.setValidBookingDateUntil(bookingDTO.validBookingDateUntil());
        booking.setValidConfirmationDateFrom(ZonedDateTime.now(ZoneId.of("Z")));
        booking.setValidConfirmationDateUntil(booking.getValidConfirmationDateFrom().plusHours(12));

        for (long id : bookingDTO.productIds()) {
            booking.addRentalGadgetProductId(id);
        }

        booking.setRequestReferenceNumber(UUID.randomUUID().toString());

        bookingRepository.save(booking);

        return booking.getRequestReferenceNumber();
    }

    public List<BookingByMonthAndProductResponseDTO> getAllBookingsByMonthAndProduct(
            BookingByMonthAndProductRequestDTO bookingByMonthAndProductRequestDTO) {
        YearMonth yearMonth = YearMonth.of(bookingByMonthAndProductRequestDTO.year(),
                bookingByMonthAndProductRequestDTO.month());

        ZonedDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay(ZoneId.of("Z"));
        ZonedDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59, 999999999).atZone(ZoneId.of("Z"));

        List<BookingModel> bookings = bookingRepository.findAllValidBookingsByMonth(startOfMonth, endOfMonth);

        if (bookings.isEmpty()) {
            return Collections.emptyList();
        }

        RentalGadgetModel rentalGadget = rentalGadgetRepository.findById(bookingByMonthAndProductRequestDTO.pId())
                .orElseThrow(() -> new RentalGadgetNotFoundException("Rental gadget listing is missing."));

        List<BookingByMonthAndProductResponseDTO> byMonthAndProductResponseDTOs = new ArrayList<>();

        for (BookingModel booking : bookings) {
            if (booking.getRentalGadgetProductIdList().contains(rentalGadget.getId())) {
                BookingByMonthAndProductResponseDTO byMonthAndProductResponseDTO = new BookingByMonthAndProductResponseDTO(
                        booking.getValidBookingDateFrom(),
                        booking.getValidBookingDateUntil());

                byMonthAndProductResponseDTOs.add(byMonthAndProductResponseDTO);
            }
        }

        return byMonthAndProductResponseDTOs;
    }

    public List<BookingByUserEmailResponseDTO> getAllBookingsByUserEmail(String email) {
        List<BookingModel> bookings = bookingRepository.findAllValidBookingsByUser(email);

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

    private boolean isBookingOverlapping(ZonedDateTime validBookingDateFrom, ZonedDateTime validBookingDateUntil) {
        List<BookingModel> allValidBookingList = bookingRepository
                .findAllValidBookingsByDuration(validBookingDateFrom, validBookingDateUntil);
        if (allValidBookingList.isEmpty()) {
            return false;
        }

        return true;
    }
}
