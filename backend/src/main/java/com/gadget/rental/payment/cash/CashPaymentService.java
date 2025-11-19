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
import com.gadget.rental.exception.InvalidBookingSequenceException;
import com.gadget.rental.exception.PaymentTransactionInProgressException;
import com.gadget.rental.exception.PaymentTransactionNotFoundException;
import com.gadget.rental.exception.RentalGadgetNotFoundException;
import com.gadget.rental.payment.PaymentItem;
import com.gadget.rental.payment.PaymentStatus;
import com.gadget.rental.payment.PaymentTransactionHistoryResponseDTO;
import com.gadget.rental.payment.PaymentTransactionModel;
import com.gadget.rental.payment.PaymentTransactionRepository;
import com.gadget.rental.rental.RentalGadgetModel;
import com.gadget.rental.rental.RentalGadgetRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CashPaymentService {

    private final RentalGadgetRepository rentalGadgetRepository;
    private final BookingRepository bookingRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final RestTemplate restTemplate;

    @Value("${booking.fee}")
    private String bookingFee;

    CashPaymentService(RentalGadgetRepository rentalGadgetRepository, BookingRepository bookingRepository,
            RestTemplate restTemplate, PaymentTransactionRepository paymentTransactionRepository) {
        this.rentalGadgetRepository = rentalGadgetRepository;
        this.bookingRepository = bookingRepository;
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.restTemplate = restTemplate;
    }

    CashPaymentReponseDTO createCashPaymentForBooking(CashPaymentDetailsDTO cashPaymentDetailsDTO) {

        List<PaymentTransactionModel> existingPaymentTransactions = paymentTransactionRepository
                .findAllPaymentTransactionsByRequestReferenceNumber(
                        cashPaymentDetailsDTO.requestReferenceNumber());

        if (existingPaymentTransactions.stream().anyMatch(transactions -> {
            return transactions.getStatus() == PaymentStatus.CASH_PAYMENT_PENDING;
        })) {
            throw new PaymentTransactionInProgressException(
                    "Payment transaction is currently in progress. Please wait and try again later.");
        }

        List<PaymentItem> itemList = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal(0);

        BookingModel booking = bookingRepository
                .findBookingByRequestReferenceNumber(cashPaymentDetailsDTO.requestReferenceNumber())
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Booking with reference number '%s' not found.",
                                cashPaymentDetailsDTO.requestReferenceNumber())));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new InvalidBookingSequenceException(
                    String.format(
                            "It appears that this booking with reference number '%s' is not in the 'PENDING' status; " +
                                    "it may have already been paid for, canceled, or finished.",
                            booking.getRequestReferenceNumber()));
        }

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

        PaymentItem bookingFeeItem = new PaymentItem();
        bookingFeeItem.setName("Booking Fee");
        bookingFeeItem.setCode(String.valueOf("BKFEE"));
        bookingFeeItem.setQuantity(1);
        bookingFeeItem.setAmount(new PaymentItem.Amount());
        bookingFeeItem.getAmount().setValue(new BigDecimal(bookingFee));
        bookingFeeItem.setTotalAmount(new PaymentItem.Amount());
        bookingFeeItem.getTotalAmount().setValue(new BigDecimal(bookingFee));
        itemList.add(bookingFeeItem);

        PaymentTransactionModel paymentTransaction = new PaymentTransactionModel();
        paymentTransaction.setTotalPrice(totalPrice);
        paymentTransaction.setCreatedBy(cashPaymentDetailsDTO.email());
        paymentTransaction.setRequestReferenceNumber(booking.getRequestReferenceNumber());
        paymentTransaction.setStatus(PaymentStatus.CASH_PAYMENT_PENDING);
        paymentTransaction.setPaymentInitiatedAt(LocalDateTime.now(ZoneId.of("Z")));
        paymentTransaction.setCheckoutId(UUID.randomUUID().toString());
        paymentTransaction.setExpiresAt(paymentTransaction.getPaymentInitiatedAt().plusHours(1).plusMinutes(30));

        paymentTransactionRepository.save(paymentTransaction);

        String message = String.format("Cash payment initiated.");

        CashPaymentReponseDTO cashPaymentReponseDTO = new CashPaymentReponseDTO(message,
                paymentTransaction.getRequestReferenceNumber(), paymentTransaction.getCheckoutId());

        return cashPaymentReponseDTO;
    }

    PaymentTransactionHistoryResponseDTO getCashPaymentForBooking(
            CashTransactionRequestDTO cashTransactionRequestDTO) {
        BookingModel booking = bookingRepository
                .findBookingByRequestReferenceNumber(cashTransactionRequestDTO.requestReferenceNumber())
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Booking with reference number '%s' not found.",
                                cashTransactionRequestDTO.requestReferenceNumber())));

        PaymentTransactionModel paymentTransaction = paymentTransactionRepository
                .findPaymentTransactionByCheckoutId(cashTransactionRequestDTO.checkoutId())
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
    String cancelCashPaymentForBooking(CashTransactionRequestDTO cashTransactionRequestDTO) {
        PaymentTransactionModel paymentTransaction = paymentTransactionRepository
                .findPaymentTransactionByCheckoutId(cashTransactionRequestDTO.checkoutId())
                .orElseThrow(() -> new PaymentTransactionNotFoundException(
                        String.format("Payment transaction with checkout id '%s' not found.",
                                cashTransactionRequestDTO.requestReferenceNumber())));

        paymentTransaction.setStatus(PaymentStatus.CASH_PAYMENT_CANCELLED);
        paymentTransactionRepository.save(paymentTransaction);

        String message = String.format("Cash payment cancelled with checkoutId '%s'.",
                paymentTransaction.getCheckoutId());

        return message;
    }

    @Transactional
    CashPaymentReponseDTO createCashDepositForRestrictedFunds(CashDepositDetailsDTO cashDepositDetailsDTO) {
        List<PaymentTransactionModel> existingPaymentTransactions = paymentTransactionRepository
                .findAllPaymentTransactionsByRequestReferenceNumber(
                        cashDepositDetailsDTO.requestReferenceNumber());

        if (existingPaymentTransactions.stream().anyMatch(transactions -> {
            return transactions.getStatus() == PaymentStatus.CASH_DEPOSIT_PENDING;
        })) {
            throw new PaymentTransactionInProgressException(
                    "Payment transaction is currently in progress. Please wait and try again later.");
        }

        BookingModel booking = bookingRepository
                .findBookingByRequestReferenceNumber(cashDepositDetailsDTO.requestReferenceNumber())
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Booking with reference number '%s' not found.",
                                cashDepositDetailsDTO.requestReferenceNumber())));

        if (booking.getStatus() != BookingStatus.APPROVED) {
            throw new InvalidBookingSequenceException(
                    String.format(
                            "The payment for booking with reference number '%s' has not been approved.",
                            booking.getRequestReferenceNumber()));
        }

        PaymentTransactionModel paymentTransaction = new PaymentTransactionModel();
        paymentTransaction.setTotalPrice(cashDepositDetailsDTO.price());
        paymentTransaction.setCreatedBy(cashDepositDetailsDTO.email());
        paymentTransaction.setRequestReferenceNumber(booking.getRequestReferenceNumber());
        paymentTransaction.setStatus(PaymentStatus.CASH_DEPOSIT_PENDING);
        paymentTransaction.setPaymentInitiatedAt(LocalDateTime.now(ZoneId.of("Z")));
        paymentTransaction.setCheckoutId(UUID.randomUUID().toString());
        paymentTransaction.setExpiresAt(paymentTransaction.getPaymentInitiatedAt().plusHours(1).plusMinutes(30));

        paymentTransactionRepository.save(paymentTransaction);

        String message = String.format("Cash deposit initiated.");

        CashPaymentReponseDTO cashPaymentReponseDTO = new CashPaymentReponseDTO(message,
                paymentTransaction.getRequestReferenceNumber(), paymentTransaction.getCheckoutId());

        return cashPaymentReponseDTO;
    }

    PaymentTransactionHistoryResponseDTO getCashDepositForBooking(
            CashTransactionRequestDTO cashTransactionRequestDTO) {
        BookingModel booking = bookingRepository
                .findBookingByRequestReferenceNumber(cashTransactionRequestDTO.requestReferenceNumber())
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Booking with reference number '%s' not found.",
                                cashTransactionRequestDTO.requestReferenceNumber())));

        PaymentTransactionModel paymentTransaction = paymentTransactionRepository
                .findPaymentTransactionByCheckoutId(cashTransactionRequestDTO.checkoutId())
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
    String cancelCashDepositForBooking(CashTransactionRequestDTO cashTransactionRequestDTO) {

        PaymentTransactionModel paymentTransaction = paymentTransactionRepository
                .findPaymentTransactionByCheckoutId(cashTransactionRequestDTO.checkoutId())
                .orElseThrow(() -> new PaymentTransactionNotFoundException(
                        String.format("Payment transaction with checkout id '%s' not found.",
                                cashTransactionRequestDTO.requestReferenceNumber())));

        paymentTransaction.setStatus(PaymentStatus.CASH_DEPOSIT_CANCELLED);

        paymentTransactionRepository.save(paymentTransaction);

        String message = String.format("Cash deposit cancelled with checkoutId '%s'.",
                paymentTransaction.getCheckoutId());

        return message;
    }
}
