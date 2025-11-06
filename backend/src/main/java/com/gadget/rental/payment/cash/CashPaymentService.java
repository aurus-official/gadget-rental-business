package com.gadget.rental.payment.cash;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.transaction.Transactional;

import com.gadget.rental.booking.BookingModel;
import com.gadget.rental.booking.BookingRepository;
import com.gadget.rental.booking.BookingStatus;
import com.gadget.rental.exception.BookingNotFoundException;
import com.gadget.rental.exception.InvalidPaymentTransactionSequenceException;
import com.gadget.rental.exception.PaymentTransactionNotFoundException;
import com.gadget.rental.exception.RentalGadgetNotFoundException;
import com.gadget.rental.payment.PaymentItem;
import com.gadget.rental.payment.PaymentStatus;
import com.gadget.rental.payment.PaymentTransactionHistoryResponseDTO;
import com.gadget.rental.payment.PaymentTransactionModel;
import com.gadget.rental.payment.PaymentTransactionRepository;
import com.gadget.rental.rental.RentalGadgetModel;
import com.gadget.rental.rental.RentalGadgetRepository;
import com.gadget.rental.rental.RentalGadgetStatus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CashPaymentService {

    private final RentalGadgetRepository rentalGadgetRepository;
    private final BookingRepository bookingRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final RestTemplate restTemplate;

    @Value("${maya.checkout.sandbox.url}")
    private String mayaCheckoutUrl;

    @Value("${maya.public.key.sandbox}")
    private String publicKey;

    @Value("${maya.secret.key.sandbox}")
    private String secretKey;

    CashPaymentService(RentalGadgetRepository rentalGadgetRepository, BookingRepository bookingRepository,
            RestTemplate restTemplate, PaymentTransactionRepository paymentTransactionRepository) {
        this.rentalGadgetRepository = rentalGadgetRepository;
        this.bookingRepository = bookingRepository;
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.restTemplate = restTemplate;
    }

    CashPaymentReponseDTO createCashPaymentForBooking(CashPaymentDetailsDTO cashPaymentDetailsDTO) {
        List<PaymentItem> itemList = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal(0);

        BookingModel booking = bookingRepository
                .findBookingByRequestReferenceNumber(cashPaymentDetailsDTO.requestReferenceNumber())
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Booking with reference number '%s' not found.",
                                cashPaymentDetailsDTO.requestReferenceNumber())));

        for (long id : booking.getRentalGadgetProductIdList()) {
            RentalGadgetModel rentalGadgetModel = rentalGadgetRepository.findById(id)
                    .orElseThrow(() -> new RentalGadgetNotFoundException("Rental gadget is missing."));

            totalPrice = totalPrice.add(rentalGadgetModel.getPrice());
            PaymentItem paymentItem = new PaymentItem();
            paymentItem.setName(rentalGadgetModel.getName());
            paymentItem.setCode(String.valueOf(rentalGadgetModel.getId()));
            paymentItem.setQuantity(1);
            paymentItem.setAmount(new PaymentItem.Amount());
            paymentItem.getAmount().setValue(rentalGadgetModel.getPrice());
            paymentItem.setTotalAmount(new PaymentItem.Amount());
            paymentItem.getTotalAmount().setValue(rentalGadgetModel.getPrice());
            itemList.add(paymentItem);
        }

        PaymentTransactionModel paymentTransaction = new PaymentTransactionModel();
        paymentTransaction.setTotalPrice(totalPrice);
        paymentTransaction.setCreatedBy(cashPaymentDetailsDTO.email());
        paymentTransaction.setRequestReferenceNumber(booking.getRequestReferenceNumber());
        paymentTransaction.setStatus(PaymentStatus.CASH_PAYMENT_PENDING);
        paymentTransaction.setPaymentInitiatedAt(LocalDateTime.now(ZoneId.of("Z")));
        paymentTransaction.setCheckoutId(UUID.randomUUID().toString());

        paymentTransactionRepository.save(paymentTransaction);

        String message = String.format("Cash payment initiated.");

        CashPaymentReponseDTO cashPaymentReponseDTO = new CashPaymentReponseDTO(message,
                paymentTransaction.getRequestReferenceNumber(), paymentTransaction.getCheckoutId());

        return cashPaymentReponseDTO;
    }

    PaymentTransactionHistoryResponseDTO getCashPaymentForBooking(
            CashPaymentTransactionRequestDTO cashPaymentTransactionRequestDTO) {
        BookingModel booking = bookingRepository
                .findBookingByRequestReferenceNumber(cashPaymentTransactionRequestDTO.requestReferenceNumber())
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Booking with reference number '%s' not found.",
                                cashPaymentTransactionRequestDTO.requestReferenceNumber())));

        PaymentTransactionModel paymentTransaction = paymentTransactionRepository
                .findPaymentTransactionByCheckoutId(cashPaymentTransactionRequestDTO.checkoutId())
                .orElseThrow(() -> new PaymentTransactionNotFoundException(
                        String.format("Payment transaction with reference number '%s' not found.",
                                booking.getRequestReferenceNumber())));

        PaymentTransactionHistoryResponseDTO paymentTransactionHistoryResponseDTO = new PaymentTransactionHistoryResponseDTO(
                paymentTransaction.getPaymentScheme(), paymentTransaction.getTotalPrice(),
                paymentTransaction.getCheckoutId(),
                paymentTransaction.getRequestReferenceNumber(), paymentTransaction.getStatus(),
                paymentTransaction.getEmail(), paymentTransaction.getCreatedBy(),
                paymentTransaction.getCurrency());

        return paymentTransactionHistoryResponseDTO;
    }

    @Transactional
    String cancelCashPaymentForBooking(CashPaymentTransactionRequestDTO cashPaymentTransactionRequestDTO) {
        BookingModel booking = bookingRepository
                .findBookingByRequestReferenceNumber(cashPaymentTransactionRequestDTO.requestReferenceNumber())
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Booking with reference number '%s' not found.",
                                cashPaymentTransactionRequestDTO.requestReferenceNumber())));

        for (Long id : booking.getRentalGadgetProductIdList()) {
            RentalGadgetModel rentalGadget = rentalGadgetRepository.findById(id)
                    .orElseThrow(() -> new RentalGadgetNotFoundException("Rental gadget listing is missing."));
            rentalGadget.setStatus(RentalGadgetStatus.AVAILABLE);
        }

        PaymentTransactionModel paymentTransaction = paymentTransactionRepository
                .findPaymentTransactionByCheckoutId(cashPaymentTransactionRequestDTO.checkoutId())
                .orElseThrow(() -> new PaymentTransactionNotFoundException(
                        String.format("Payment transaction with checkout id '%s' not found.",
                                booking.getRequestReferenceNumber())));

        paymentTransaction.setStatus(PaymentStatus.CASH_PAYMENT_CANCELLED);

        String message = String.format("Cash payment cancelled with checkoutId '%s'.",
                paymentTransaction.getCheckoutId());

        return message;
    }

    @Transactional
    CashPaymentReponseDTO createCashDepositForRestrictedFunds(CashDepositDetailsDTO cashDepositDetailsDTO,
            String requestReferenceNumber) {

        BookingModel booking = bookingRepository
                .findBookingByRequestReferenceNumber(requestReferenceNumber)
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Booking with reference number '%s' not found.",
                                requestReferenceNumber)));

        if (booking.getStatus() != BookingStatus.PAYMENT_CONFIRMED) {
            throw new InvalidPaymentTransactionSequenceException(
                    String.format(
                            "The payment transaction with reference number '%s' has not been completed for this booking.",
                            booking.getRequestReferenceNumber()));
        }

        PaymentTransactionModel paymentTransaction = new PaymentTransactionModel();
        paymentTransaction.setTotalPrice(cashDepositDetailsDTO.price());
        paymentTransaction.setCreatedBy(cashDepositDetailsDTO.email());
        paymentTransaction.setRequestReferenceNumber(booking.getRequestReferenceNumber());
        paymentTransaction.setStatus(PaymentStatus.CASH_DEPOSIT_PENDING);
        paymentTransaction.setPaymentInitiatedAt(LocalDateTime.now(ZoneId.of("Z")));
        paymentTransaction.setCheckoutId(UUID.randomUUID().toString());

        paymentTransactionRepository.save(paymentTransaction);

        String message = String.format("Cash deposit initiated.");

        CashPaymentReponseDTO cashPaymentReponseDTO = new CashPaymentReponseDTO(message,
                paymentTransaction.getRequestReferenceNumber(), paymentTransaction.getCheckoutId());

        return cashPaymentReponseDTO;
    }
}
