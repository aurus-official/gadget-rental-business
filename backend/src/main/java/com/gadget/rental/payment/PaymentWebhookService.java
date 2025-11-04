package com.gadget.rental.payment;

import jakarta.transaction.Transactional;

import com.gadget.rental.booking.BookingModel;
import com.gadget.rental.booking.BookingRepository;
import com.gadget.rental.exception.BookingNotFoundException;
import com.gadget.rental.exception.PaymentTransactionNotFoundException;
import com.gadget.rental.exception.RentalGadgetNotFoundException;
import com.gadget.rental.rental.RentalGadgetModel;
import com.gadget.rental.rental.RentalGadgetRepository;
import com.gadget.rental.rental.RentalGadgetStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentWebhookService {
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final BookingRepository bookingRepository;
    private final RentalGadgetRepository rentalGadgetRepository;

    @Autowired
    PaymentWebhookService(PaymentTransactionRepository paymentTransactionRepository,
            BookingRepository bookingRepository, RentalGadgetRepository rentalGadgetRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.bookingRepository = bookingRepository;
        this.rentalGadgetRepository = rentalGadgetRepository;
    }

    @Transactional
    public void addSuccessfulOnlinePaymentToTransactions(
            OnlinePaymentWebhookPayloadRequestDTO onlinePaymentWebhookPayloadRequestDTO) {
        PaymentTransactionModel paymentTransaction = paymentTransactionRepository
                .findPaymentTransactionByRequestReferenceNumber(
                        onlinePaymentWebhookPayloadRequestDTO.getRequestReferenceNumber())
                .orElseThrow(() -> new PaymentTransactionNotFoundException(
                        String.format("Payment transaction with reference number \"%s\" not found.",
                                onlinePaymentWebhookPayloadRequestDTO.getRequestReferenceNumber())));
        paymentTransaction
                .setPaymentScheme(onlinePaymentWebhookPayloadRequestDTO.getFundSource().getDetails().getScheme());
        paymentTransaction.setStatus(PaymentStatus.PAYMENT_SUCCESS);
        paymentTransaction.setTotalPrice(Double.valueOf(onlinePaymentWebhookPayloadRequestDTO.getAmount()));
        paymentTransaction.setCurrency(onlinePaymentWebhookPayloadRequestDTO.getCurrency());
        paymentTransaction.setPaymentInitiatedAt(onlinePaymentWebhookPayloadRequestDTO.getCreatedAt());
        paymentTransaction.setLastUpdate(onlinePaymentWebhookPayloadRequestDTO.getUpdatedAt());
        paymentTransaction.setRequestReferenceNumber(onlinePaymentWebhookPayloadRequestDTO.getRequestReferenceNumber());
        paymentTransaction
                .setTransactionReferenceNumber(onlinePaymentWebhookPayloadRequestDTO.getReceipt().getTransactionId());
        paymentTransaction.setReceiptNumber(onlinePaymentWebhookPayloadRequestDTO.getReceiptNumber());
        paymentTransaction.setCheckoutId(onlinePaymentWebhookPayloadRequestDTO.getId());

        BookingModel booking = bookingRepository
                .findBookingByRequestReferenceNumber(paymentTransaction.getRequestReferenceNumber())
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Booking with reference number \"%s\" not found.",
                                onlinePaymentWebhookPayloadRequestDTO.getRequestReferenceNumber())));

        paymentTransaction.setEmail(booking.getCreatedFor());
        paymentTransaction.setCreatedBy(booking.getCreatedBy());

        for (long id : booking.getRentalGadgetProductIdList()) {
            RentalGadgetModel rentalGadget = rentalGadgetRepository.findById(id)
                    .orElseThrow(() -> new RentalGadgetNotFoundException("Rental gadget listing is missing."));
            rentalGadget.setStatus(RentalGadgetStatus.BOOKED_PAID);
        }

        paymentTransactionRepository.save(paymentTransaction);
    }

    @Transactional
    public void addSuccessfulCashPaymentToTransactions(
            CashPaymentWebhookPayloadRequestDTO cashPaymentWebhookPayloadRequestDTO) {
        // TODO: Compare total price and the paid price

        PaymentTransactionModel paymentTransaction = paymentTransactionRepository
                .findPaymentTransactionByRequestReferenceNumber(
                        cashPaymentWebhookPayloadRequestDTO.getRequestReferenceNumber())
                .orElseThrow(() -> new PaymentTransactionNotFoundException(
                        String.format("Payment transaction with reference number \"%s\" not found.",
                                cashPaymentWebhookPayloadRequestDTO.getRequestReferenceNumber())));
        paymentTransaction
                .setPaymentScheme(cashPaymentWebhookPayloadRequestDTO.getFundSource().getDetails().getScheme());
        paymentTransaction.setStatus(PaymentStatus.PAYMENT_SUCCESS);
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

        for (long id : booking.getRentalGadgetProductIdList()) {
            RentalGadgetModel rentalGadget = rentalGadgetRepository.findById(id)
                    .orElseThrow(() -> new RentalGadgetNotFoundException("Rental gadget listing is missing."));
            rentalGadget.setStatus(RentalGadgetStatus.BOOKED_PAID);
        }

        paymentTransactionRepository.save(paymentTransaction);
    }
}
