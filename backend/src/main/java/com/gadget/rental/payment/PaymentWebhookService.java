package com.gadget.rental.payment;

import jakarta.transaction.Transactional;

import com.gadget.rental.booking.BookingModel;
import com.gadget.rental.booking.BookingRepository;
import com.gadget.rental.exception.BookingNotFoundException;
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
    public void addSuccessfulPaymentToTransactions(PaymentWebhookResponse paymentWebhookResponse) {
        PaymentTransactionModel paymentTransaction = new PaymentTransactionModel();
        paymentTransaction.setPaymentScheme(paymentWebhookResponse.getFundSource().getDetails().getScheme());
        paymentTransaction.setStatus(PaymentStatus.PAYMENT_SUCCESS);
        paymentTransaction.setTotalPrice(Double.valueOf(paymentWebhookResponse.getAmount()));
        paymentTransaction.setCurrency(paymentWebhookResponse.getCurrency());
        paymentTransaction.setPaymentInitiatedAt(paymentWebhookResponse.getCreatedAt());
        paymentTransaction.setLastUpdate(paymentWebhookResponse.getUpdatedAt());
        paymentTransaction.setRequestReferenceNumber(paymentWebhookResponse.getRequestReferenceNumber());
        paymentTransaction.setTransactionReferenceNumber(paymentWebhookResponse.getReceipt().getTransactionId());
        paymentTransaction.setReceiptNumber(paymentWebhookResponse.getReceiptNumber());
        paymentTransaction.setCheckoutId(paymentWebhookResponse.getId());

        BookingModel booking = bookingRepository
                .findBookingByRequestReferenceNumber(paymentTransaction.getRequestReferenceNumber())
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Booking with reference number \"%s\" not found.",
                                paymentWebhookResponse.getRequestReferenceNumber())));

        paymentTransaction.setEmail(booking.getCreatedFor());

        for (long id : booking.getRentalGadgetProductIdList()) {
            RentalGadgetModel rentalGadget = rentalGadgetRepository.findById(id)
                    .orElseThrow(() -> new RentalGadgetNotFoundException("Rental gadget listing is missing."));
            rentalGadget.setStatus(RentalGadgetStatus.BOOKED_PAID);
        }

        paymentTransactionRepository.save(paymentTransaction);
    }
}
