package com.gadget.rental.configuration;

import com.gadget.rental.booking.BookingCleanupTask;
import com.gadget.rental.booking.BookingRepository;
import com.gadget.rental.rental.RentalGadgetRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookingCleanupTaskConfig {

    @Bean
    BookingCleanupTask getBookingCleanupTask(BookingRepository bookingRepository,
            RentalGadgetRepository rentalGadgetRepository) {
        return new BookingCleanupTask(bookingRepository, rentalGadgetRepository);
    }
}
