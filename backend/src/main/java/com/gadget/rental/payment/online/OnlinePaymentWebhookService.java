package com.gadget.rental.payment.online;

import java.math.BigDecimal;

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
public class OnlinePaymentWebhookService {
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final BookingRepository bookingRepository;
    private final RentalGadgetRepository rentalGadgetRepository;

    @Autowired
    OnlinePaymentWebhookService(PaymentTransactionRepository paymentTransactionRepository,
            BookingRepository bookingRepository, RentalGadgetRepository rentalGadgetRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.bookingRepository = bookingRepository;
        this.rentalGadgetRepository = rentalGadgetRepository;
    }

    @Transactional
    public String addSuccessfulOnlinePaymentToTransactions(
            OnlinePaymentWebhookPayloadRequestDTO onlinePaymentWebhookPayloadRequestDTO) {

        BookingModel booking = bookingRepository
                .findBookingByRequestReferenceNumber(onlinePaymentWebhookPayloadRequestDTO.getRequestReferenceNumber())
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Booking with reference number '%s' not found.",
                                onlinePaymentWebhookPayloadRequestDTO.getRequestReferenceNumber())));

        PaymentTransactionModel paymentTransaction = paymentTransactionRepository
                .findPaymentTransactionByCheckoutId(
                        onlinePaymentWebhookPayloadRequestDTO.getId())
                .orElseThrow(() -> new PaymentTransactionNotFoundException(
                        String.format("Payment transaction with checkout id \"%s\" not found.",
                                onlinePaymentWebhookPayloadRequestDTO.getId())));
        paymentTransaction
                .setPaymentScheme(onlinePaymentWebhookPayloadRequestDTO.getFundSource().getDetails().getScheme());
        paymentTransaction.setStatus(PaymentStatus.ONLINE_PAYMENT_CONFIRMED);
        paymentTransaction.setTotalPrice(new BigDecimal(onlinePaymentWebhookPayloadRequestDTO.getAmount()));
        paymentTransaction.setCurrency(onlinePaymentWebhookPayloadRequestDTO.getCurrency());
        paymentTransaction.setPaymentInitiatedAt(onlinePaymentWebhookPayloadRequestDTO.getCreatedAt());
        paymentTransaction.setLastUpdate(onlinePaymentWebhookPayloadRequestDTO.getUpdatedAt());
        paymentTransaction.setRequestReferenceNumber(onlinePaymentWebhookPayloadRequestDTO.getRequestReferenceNumber());
        paymentTransaction
                .setTransactionReferenceNumber(onlinePaymentWebhookPayloadRequestDTO.getReceipt().getTransactionId());
        paymentTransaction.setReceiptNumber(onlinePaymentWebhookPayloadRequestDTO.getReceiptNumber());
        paymentTransaction.setCheckoutId(onlinePaymentWebhookPayloadRequestDTO.getId());

        paymentTransaction.setEmail(booking.getCreatedFor());
        paymentTransaction.setCreatedBy(booking.getCreatedBy());

        booking.setStatus(BookingStatus.PAYMENT_CONFIRMED);

        for (long id : booking.getRentalGadgetProductIdList()) {
            RentalGadgetModel rentalGadget = rentalGadgetRepository.findById(id)
                    .orElseThrow(() -> new RentalGadgetNotFoundException("Rental gadget listing is missing."));
            rentalGadget.setStatus(RentalGadgetStatus.NOT_AVAILABLE);
        }

        paymentTransactionRepository.save(paymentTransaction);
        bookingRepository.save(booking);

        String message = String.format("Cash payment confirmed with request reference number '%s'.",
                booking.getRequestReferenceNumber());

        return message;
    }
}
