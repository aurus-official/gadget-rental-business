package com.gadget.rental.booking;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public String createBookingToGetReferenceNumber(BookingDTO bookingDTO) {

        if (isBookingOverlapping(bookingDTO)) {
            throw new RuntimeException();
        }

        return "";
    }

    private boolean isBookingOverlapping(BookingDTO bookingDTO) {
        List<BookingModel> allValidBookingList = bookingRepository
                .findAllValidBookings(bookingDTO.validBookingDateFrom(), bookingDTO.validBookingDateUntil());
        if (allValidBookingList.isEmpty()) {
            return true;
        }
        return false;
    }
}
