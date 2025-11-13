package com.gadget.rental.payment;

import java.util.ArrayList;
import java.util.List;

import com.gadget.rental.booking.BookingModel;
import com.gadget.rental.booking.BookingRepository;
import com.gadget.rental.exception.BookingNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final BookingRepository bookingRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;

    PaymentService(BookingRepository bookingRepository,
            PaymentTransactionRepository paymentTransactionRepository) {
        this.bookingRepository = bookingRepository;
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    List<PaymentTransactionHistoryResponseDTO> getAllPaymentTransactionsHistoryByRequestReferenceNumber(
            PaymentTransactionHistoryRequestDTO paymentTransactionHistoryRequestDTO) {

        BookingModel booking = bookingRepository
                .findBookingByRequestReferenceNumber(paymentTransactionHistoryRequestDTO.requestReferenceNumber())
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Booking with reference number '%s' not found.",
                                paymentTransactionHistoryRequestDTO.requestReferenceNumber())));

        List<PaymentTransactionModel> paymentTransactions = paymentTransactionRepository
                .findAllPaymentTransactionsByRequestReferenceNumber(booking.getRequestReferenceNumber());

        List<PaymentTransactionHistoryResponseDTO> paymentTransactionHistoryResponseDTOs = new ArrayList<>();

        for (PaymentTransactionModel paymentTransaction : paymentTransactions) {
            PaymentTransactionHistoryResponseDTO paymentTransactionHistoryResponseDTO = new PaymentTransactionHistoryResponseDTO(
                    paymentTransaction.getPaymentScheme(), paymentTransaction.getTotalPrice(),
                    paymentTransaction.getCheckoutId(),
                    paymentTransaction.getRequestReferenceNumber(), paymentTransaction.getStatus(),
                    paymentTransaction.getEmail(), paymentTransaction.getCreatedBy(),
                    paymentTransaction.getCurrency());

            paymentTransactionHistoryResponseDTOs.add(paymentTransactionHistoryResponseDTO);
        }

        return paymentTransactionHistoryResponseDTOs;
    }

}
