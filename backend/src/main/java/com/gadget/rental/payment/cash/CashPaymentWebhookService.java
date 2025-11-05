package com.gadget.rental.payment.cash;

import jakarta.transaction.Transactional;

import com.gadget.rental.booking.BookingModel;
import com.gadget.rental.booking.BookingRepository;
import com.gadget.rental.booking.BookingStatus;
import com.gadget.rental.exception.BookingNotFoundException;
import com.gadget.rental.exception.PaymentTransactionNotFoundException;
import com.gadget.rental.exception.RentalGadgetNotFoundException;
import com.gadget.rental.payment.PaymentStatus;
import com.gadget.rental.payment.PaymentTransactionModel;
import com.gadget.rental.payment.PaymentTransactionRepository;
import com.gadget.rental.rental.RentalGadgetModel;
import com.gadget.rental.rental.RentalGadgetRepository;
import com.gadget.rental.rental.RentalGadgetStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CashPaymentWebhookService {
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final BookingRepository bookingRepository;
    private final RentalGadgetRepository rentalGadgetRepository;

    @Autowired
    CashPaymentWebhookService(PaymentTransactionRepository paymentTransactionRepository,
            BookingRepository bookingRepository, RentalGadgetRepository rentalGadgetRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.bookingRepository = bookingRepository;
        this.rentalGadgetRepository = rentalGadgetRepository;
    }

    @Transactional
    public String addSuccessfulCashPaymentToTransactions(
            CashPaymentWebhookPayloadRequestDTO cashPaymentWebhookPayloadRequestDTO) {

        PaymentTransactionModel paymentTransaction = paymentTransactionRepository
                .findPaymentTransactionByRequestReferenceNumber(
                        cashPaymentWebhookPayloadRequestDTO.getRequestReferenceNumber())
                .orElseThrow(() -> new PaymentTransactionNotFoundException(
                        String.format("Payment transaction with reference number \"%s\" not found.",
                                cashPaymentWebhookPayloadRequestDTO.getRequestReferenceNumber())));

        // TODO: Check if amount is the same with required amount

        paymentTransaction
                .setPaymentScheme(cashPaymentWebhookPayloadRequestDTO.getFundSource().getDetails().getScheme());
        paymentTransaction.setStatus(PaymentStatus.CASH_PAYMENT_CONFIRMED);
        paymentTransaction.setTotalPrice(Double.valueOf(cashPaymentWebhookPayloadRequestDTO.getAmount()));
        paymentTransaction.setCurrency(cashPaymentWebhookPayloadRequestDTO.getCurrency());
        paymentTransaction.setPaymentInitiatedAt(cashPaymentWebhookPayloadRequestDTO.getCreatedAt());
        paymentTransaction.setLastUpdate(cashPaymentWebhookPayloadRequestDTO.getUpdatedAt());
        paymentTransaction.setRequestReferenceNumber(cashPaymentWebhookPayloadRequestDTO.getRequestReferenceNumber());
        paymentTransaction
                .setTransactionReferenceNumber(cashPaymentWebhookPayloadRequestDTO.getReceipt().getTransactionId());
        paymentTransaction.setReceiptNumber(cashPaymentWebhookPayloadRequestDTO.getReceiptNumber());
        paymentTransaction.setCheckoutId(cashPaymentWebhookPayloadRequestDTO.getId());

        BookingModel booking = bookingRepository
                .findBookingByRequestReferenceNumber(paymentTransaction.getRequestReferenceNumber())
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Booking with reference number \"%s\" not found.",
                                cashPaymentWebhookPayloadRequestDTO.getRequestReferenceNumber())));

        paymentTransaction.setEmail(booking.getCreatedFor());
        paymentTransaction.setCreatedBy(booking.getCreatedBy());

        booking.setStatus(BookingStatus.PAYMENT_CONFIRMED);

        for (long id : booking.getRentalGadgetProductIdList()) {
            RentalGadgetModel rentalGadget = rentalGadgetRepository.findById(id)
                    .orElseThrow(() -> new RentalGadgetNotFoundException("Rental gadget listing is missing."));
            rentalGadget.setStatus(RentalGadgetStatus.NOT_AVAILABLE);
        }

        bookingRepository.save(booking);
        paymentTransactionRepository.save(paymentTransaction);

        String message = String.format("Cash payment confirmed with request reference number '%s'.",
                booking.getRequestReferenceNumber());

        return message;
    }

    @Transactional
    public String addSuccessfulCashDepositToTransactions(
            CashPaymentWebhookPayloadRequestDTO cashPaymentWebhookPayloadRequestDTO) {

        PaymentTransactionModel paymentTransaction = paymentTransactionRepository
                .findPaymentTransactionByRequestReferenceNumber(
                        cashPaymentWebhookPayloadRequestDTO.getRequestReferenceNumber())
                .orElseThrow(() -> new PaymentTransactionNotFoundException(
                        String.format("Payment transaction with reference number \"%s\" not found.",
                                cashPaymentWebhookPayloadRequestDTO.getRequestReferenceNumber())));

        paymentTransaction
                .setPaymentScheme(cashPaymentWebhookPayloadRequestDTO.getFundSource().getDetails().getScheme());
        paymentTransaction.setStatus(PaymentStatus.CASH_DEPOSIT_CONFIRMED);
        paymentTransaction.setTotalPrice(Double.valueOf(cashPaymentWebhookPayloadRequestDTO.getAmount()));
        paymentTransaction.setCurrency(cashPaymentWebhookPayloadRequestDTO.getCurrency());
        paymentTransaction.setPaymentInitiatedAt(cashPaymentWebhookPayloadRequestDTO.getCreatedAt());
        paymentTransaction.setLastUpdate(cashPaymentWebhookPayloadRequestDTO.getUpdatedAt());
        paymentTransaction.setRequestReferenceNumber(cashPaymentWebhookPayloadRequestDTO.getRequestReferenceNumber());
        paymentTransaction
                .setTransactionReferenceNumber(cashPaymentWebhookPayloadRequestDTO.getReceipt().getTransactionId());
        paymentTransaction.setReceiptNumber(cashPaymentWebhookPayloadRequestDTO.getReceiptNumber());
        paymentTransaction.setCheckoutId(cashPaymentWebhookPayloadRequestDTO.getId());

        BookingModel booking = bookingRepository
                .findBookingByRequestReferenceNumber(paymentTransaction.getRequestReferenceNumber())
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Booking with reference number \"%s\" not found.",
                                cashPaymentWebhookPayloadRequestDTO.getRequestReferenceNumber())));

        paymentTransaction.setEmail(booking.getCreatedFor());
        paymentTransaction.setCreatedBy(booking.getCreatedBy());

        booking.setStatus(BookingStatus.RESTRICTED_FUNDS_CONFIRMED);

        for (long id : booking.getRentalGadgetProductIdList()) {
            RentalGadgetModel rentalGadget = rentalGadgetRepository.findById(id)
                    .orElseThrow(() -> new RentalGadgetNotFoundException("Rental gadget listing is missing."));
            rentalGadget.setStatus(RentalGadgetStatus.NOT_AVAILABLE);
        }

        bookingRepository.save(booking);
        paymentTransactionRepository.save(paymentTransaction);

        String message = String.format("Cash payment confirmed with request reference number '%s'.",
                booking.getRequestReferenceNumber());

        return message;
    }
}
