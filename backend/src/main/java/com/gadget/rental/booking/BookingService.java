package com.gadget.rental.booking;

import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gadget.rental.exception.RentalGadgetNotFoundException;
import com.gadget.rental.rental.RentalGadgetModel;
import com.gadget.rental.rental.RentalGadgetRepository;

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
}
