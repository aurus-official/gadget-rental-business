package com.gadget.rental.id;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.mail.MessagingException;

import com.gadget.rental.booking.BookingModel;
import com.gadget.rental.booking.BookingRepository;
import com.gadget.rental.booking.BookingStatus;
import com.gadget.rental.exception.BookingAlreadyApprovedException;
import com.gadget.rental.exception.BookingNotFoundException;
import com.gadget.rental.exception.InvalidBookingSequenceException;
import com.gadget.rental.payment.PaymentStatus;
import com.gadget.rental.payment.PaymentTransactionModel;
import com.gadget.rental.payment.PaymentTransactionRepository;
import com.gadget.rental.shared.EmailSenderService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IdService {
    @Value("${valid.id.path}")
    private String validIdPath;

    private BookingRepository bookingRepository;
    private EmailSenderService emailSenderService;
    private PaymentTransactionRepository paymentTransactionRepository;

    public IdService(BookingRepository bookingRepository, EmailSenderService emailSenderService,
            PaymentTransactionRepository paymentTransactionRepository) {
        this.bookingRepository = bookingRepository;
        this.emailSenderService = emailSenderService;
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    public List<byte[]> getIdImagesByRequestReferenceNumber(String requestReferenceNumber) {
        BookingModel booking = bookingRepository
                .findBookingByRequestReferenceNumber(requestReferenceNumber)
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Booking with reference number '%s' not found.",
                                requestReferenceNumber)));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BookingAlreadyApprovedException(
                    String.format("Booking with reference number '%s' has already been approved.",
                            requestReferenceNumber));
        }

        Path idImagesBookingDir = Paths.get(String.format("%s/%s/", validIdPath, requestReferenceNumber));
        List<byte[]> idImages = new ArrayList<>();

        try {
            for (Path imagePath : Files.list(idImagesBookingDir).collect(Collectors.toList())) {
                if (Files.isRegularFile(imagePath)) {
                    idImages.add(Files.readAllBytes(imagePath));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return idImages;
    }

    public String approveBookingByRequestReferenceNumber(String requestReferenceNumber) {
        BookingModel booking = bookingRepository
                .findBookingByRequestReferenceNumber(requestReferenceNumber)
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Booking with reference number '%s' not found.",
                                requestReferenceNumber)));

        if (booking.getStatus() != BookingStatus.PAYMENT_CONFIRMED) {
            throw new BookingAlreadyApprovedException(
                    String.format("Booking with reference number '%s' has already been approved.",
                            requestReferenceNumber));
        }

        List<PaymentTransactionModel> paymentTransactions = paymentTransactionRepository
                .findAllPaymentTransactionsByRequestReferenceNumber(booking.getRequestReferenceNumber());

        Optional<PaymentTransactionModel> paidStatusPaymentTransaction = paymentTransactions.stream()
                .filter((pTr) -> pTr.getStatus() == PaymentStatus.ONLINE_PAYMENT_CONFIRMED
                        || pTr.getStatus() == PaymentStatus.CASH_PAYMENT_CONFIRMED)
                .findFirst();

        if (paidStatusPaymentTransaction.isEmpty()) {
            throw new InvalidBookingSequenceException(
                    String.format(
                            "This booking with reference number '%s' has not yet been paid",
                            booking.getRequestReferenceNumber()));
        }

        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);

        try {
            emailSenderService.sendConfirmationSignedContract(booking.getCreatedFor(),
                    booking.getRequestReferenceNumber(),
                    booking.getValidBookingDateFrom(), booking.getValidBookingDateUntil(),
                    paidStatusPaymentTransaction.get().getTotalPrice());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return String.format("Booking with reference number '%s' has been approved.",
                requestReferenceNumber);
    }
}
