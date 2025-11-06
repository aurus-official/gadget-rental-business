package com.gadget.rental.payment;

import com.gadget.rental.booking.BookingRepository;
import com.gadget.rental.rental.RentalGadgetRepository;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final RentalGadgetRepository rentalGadgetRepository;
    private final BookingRepository bookingRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;

    PaymentService(RentalGadgetRepository rentalGadgetRepository, BookingRepository bookingRepository,
            PaymentTransactionRepository paymentTransactionRepository) {
        this.rentalGadgetRepository = rentalGadgetRepository;
        this.bookingRepository = bookingRepository;
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    PaymentTransactionHistoryResponseDTO getAllPaymentTransactionsHistoryByRequestReferenceNumber(
            PaymentTransactionHistoryRequestDTO paymentTransactionHistoryRequestDTO) {

        return null;
    }

}
