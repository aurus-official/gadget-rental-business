package com.gadget.rental.booking;

import java.util.List;
import java.util.Optional;

import com.gadget.rental.rental.RentalGadgetModel;
import com.gadget.rental.rental.RentalGadgetRepository;
import com.gadget.rental.rental.RentalGadgetStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class BookingCleanupTask {

    private BookingRepository bookingRepository;
    private RentalGadgetRepository rentalGadgetRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(BookingCleanupTask.class);

    @Autowired
    public BookingCleanupTask(BookingRepository bookingRepository, RentalGadgetRepository rentalGadgetRepository) {
        this.bookingRepository = bookingRepository;
        this.rentalGadgetRepository = rentalGadgetRepository;
    }

    @Scheduled(fixedDelay = 600_000)
    void resetExpiredBookings() {
        List<BookingModel> expiredBookings = bookingRepository.findAllExpiredBookings();

        if (expiredBookings.size() == 0) {
            return;
        }

        expiredBookings.stream().forEach((booking) -> {
            booking.setStatus(BookingStatus.EXPIRED);
            List<Long> productIds = booking.getRentalGadgetProductIdList();

            productIds.stream().forEach((id) -> {
                Optional<RentalGadgetModel> rentalGadgetOptional = rentalGadgetRepository.findById(id);

                if (rentalGadgetOptional.isEmpty()) {
                    return;
                }

                RentalGadgetModel rentalGadget = rentalGadgetOptional.get();

                if (rentalGadget.getStatus() != RentalGadgetStatus.AVAILABLE) {
                    rentalGadget.setStatus(RentalGadgetStatus.AVAILABLE);
                    rentalGadgetRepository.save(rentalGadget);
                }
            });
        });
    }

    @Scheduled(cron = "0 0 0 1 1/6 ?")
    void deleteExpiredBooking() {
        List<BookingModel> expiredBookings = bookingRepository.findAllExpiredBookings();

        if (expiredBookings.size() == 0) {
            return;
        }

        expiredBookings.stream().forEach((transaction) -> {
            bookingRepository.delete(transaction);
        });

    }

}
