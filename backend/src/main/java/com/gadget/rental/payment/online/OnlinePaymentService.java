package com.gadget.rental.payment.online;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import com.gadget.rental.auth.jwt.JwtAuthenticationToken;
import com.gadget.rental.booking.BookingModel;
import com.gadget.rental.booking.BookingRepository;
import com.gadget.rental.booking.BookingStatus;
import com.gadget.rental.exception.BookingNotFoundException;
import com.gadget.rental.exception.InvalidBookingSequenceException;
import com.gadget.rental.exception.PaymentTransactionInProgressException;
import com.gadget.rental.exception.RentalGadgetNotFoundException;
import com.gadget.rental.payment.PaymentItem;
import com.gadget.rental.payment.PaymentStatus;
import com.gadget.rental.payment.PaymentTransactionHistoryResponseDTO;
import com.gadget.rental.payment.PaymentTransactionModel;
import com.gadget.rental.payment.PaymentTransactionRepository;
import com.gadget.rental.payment.online.OnlineCheckoutRequestDTO.Buyer.Contact;
import com.gadget.rental.rental.RentalGadgetModel;
import com.gadget.rental.rental.RentalGadgetRepository;
import com.gadget.rental.shared.Base64Util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OnlinePaymentService {

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

    @Value("${booking.fee}")
    private String bookingFee;

    OnlinePaymentService(RentalGadgetRepository rentalGadgetRepository, BookingRepository bookingRepository,
            RestTemplate restTemplate, PaymentTransactionRepository paymentTransactionRepository) {
        this.rentalGadgetRepository = rentalGadgetRepository;
        this.bookingRepository = bookingRepository;
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    OnlineCheckoutResponseDTO createOnlinePaymentForBooking(OnlinePaymentDetailsDTO onlinePaymentDetailsDTO) {
        List<PaymentTransactionModel> existingPaymentTransactions = paymentTransactionRepository
                .findAllPaymentTransactionsByRequestReferenceNumber(
                        onlinePaymentDetailsDTO.requestReferenceNumber());

        if (existingPaymentTransactions.stream().anyMatch(transactions -> {
            return transactions.getStatus() == PaymentStatus.CASH_DEPOSIT_PENDING;
        })) {
            throw new PaymentTransactionInProgressException(
                    "Payment transaction is currently in progress. Please wait and try again later.");
        }
        SecurityContext securityContext = SecurityContextHolder.getContext();
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) securityContext.getAuthentication();

        List<PaymentItem> itemList = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal(0);

        BookingModel booking = bookingRepository
                .findBookingByRequestReferenceNumber(onlinePaymentDetailsDTO.requestReferenceNumber())
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Booking with reference number '%s' not found.",
                                onlinePaymentDetailsDTO.requestReferenceNumber())));

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

        System.out.println(totalPrice);
        OnlineCheckoutRequestDTO onlineCheckoutRequestDTO = new OnlineCheckoutRequestDTO();
        onlineCheckoutRequestDTO.setTotalAmount(new OnlineCheckoutRequestDTO.TotalAmount());
        onlineCheckoutRequestDTO.getTotalAmount().setValue(totalPrice);
        onlineCheckoutRequestDTO.getTotalAmount().setCurrency("PHP");
        onlineCheckoutRequestDTO.setBuyer(new OnlineCheckoutRequestDTO.Buyer());
        onlineCheckoutRequestDTO.getBuyer().setFirstName(onlinePaymentDetailsDTO.firstName());
        onlineCheckoutRequestDTO.getBuyer().setLastName(onlinePaymentDetailsDTO.lastName());
        onlineCheckoutRequestDTO.getBuyer().setContact(new Contact());
        onlineCheckoutRequestDTO.getBuyer().getContact().setEmail(jwtAuthenticationToken.getName());
        onlineCheckoutRequestDTO.getBuyer().getContact().setPhone(onlinePaymentDetailsDTO.phoneNumber());
        onlineCheckoutRequestDTO.setRequestReferenceNumber(booking.getRequestReferenceNumber());
        onlineCheckoutRequestDTO.setItemList(itemList);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization",
                String.format("Basic %s", Base64Util.encodeBase64(String.format("%s:", publicKey).getBytes())));
        HttpEntity<OnlineCheckoutRequestDTO> httpEntity = new HttpEntity<>(onlineCheckoutRequestDTO, httpHeaders);
        ResponseEntity<OnlineSucessPaymentPayloadResponseDTO> responseEntity = restTemplate.postForEntity(
                mayaCheckoutUrl,
                httpEntity,
                OnlineSucessPaymentPayloadResponseDTO.class);

        PaymentTransactionModel paymentTransaction = new PaymentTransactionModel();
        paymentTransaction.setTotalPrice(totalPrice);
        paymentTransaction.setCreatedBy(jwtAuthenticationToken.getName());
        paymentTransaction.setRequestReferenceNumber(booking.getRequestReferenceNumber());
        paymentTransaction.setStatus(PaymentStatus.ONLINE_PAYMENT_PENDING);
        paymentTransaction.setCheckoutId(responseEntity.getBody().getCheckoutId());
        paymentTransaction.setExpiresAt(paymentTransaction.getPaymentInitiatedAt().plusHours(1).plusMinutes(30));

        paymentTransactionRepository.save(paymentTransaction);

        OnlineCheckoutResponseDTO onlineCheckoutResponseDTO = new OnlineCheckoutResponseDTO(
                booking.getRequestReferenceNumber(), responseEntity.getBody().getCheckoutId(),
                responseEntity.getBody().getRedirectUrl(), jwtAuthenticationToken.getName());

        return onlineCheckoutResponseDTO;
    }

    PaymentTransactionHistoryResponseDTO getOnlinePaymentForBooking(
            OnlinePaymentTransactionRequestDTO onlinePaymentTransactionRequestDTO) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) securityContext.getAuthentication();

        // TODO : Check if email in jwtauthtoken matches the returned payment data;

        return null;
    }

    String cancelOnlinePaymentForBooking(OnlinePaymentTransactionRequestDTO onlinePaymentTransactionRequestDTO) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) securityContext.getAuthentication();

        // TODO : Cancel the online payment through paymentId aka the checkoutId

        return "";
    }

    @Transactional
    String createOnlinePreAuthForRestrictedFunds(OnlinePaymentDetailsDTO onlinePaymentDetailsDTO) {
        // List<PaymentTransactionModel> existingPaymentTransactions =
        // paymentTransactionRepository
        // .findAllPaymentTransactionByRequestReferenceNumber(
        // cashDepositDetailsDTO.requestReferenceNumber());
        //
        // if (existingPaymentTransactions.stream().anyMatch(transactions -> {
        // return transactions.getStatus() == PaymentStatus.CASH_DEPOSIT_PENDING;
        // })) {
        // throw new PaymentTransactionInProgressException(
        // "Payment transaction is currently in progress. Please wait and try again
        // later.");
        // }
        // BookingModel booking = bookingRepository
        // .findBookingByRequestReferenceNumber(paymentTransactionRequestDTO.requestReferenceNumber())
        // .orElseThrow(() -> new BookingNotFoundException(
        // String.format("Booking with reference number '%s' not found.",
        // paymentTransactionRequestDTO.requestReferenceNumber())));
        //
        // if (booking.getStatus() != BookingStatus.PENDING) {
        // throw new InvalidBookingSequenceException(
        // String.format(
        // "It appears that this booking with reference number '%s' is not in the
        // 'PENDING' status; " +
        // "it may have already been paid for, canceled, or finished.",
        // booking.getRequestReferenceNumber()));
        // }
        //
        // for (Long id : booking.getRentalGadgetProductIdList()) {
        // RentalGadgetModel rentalGadget = rentalGadgetRepository.findById(id)
        // .orElseThrow(() -> new RentalGadgetNotFoundException("Rental gadget listing
        // is missing."));
        // rentalGadget.setStatus(RentalGadgetStatus.AVAILABLE);
        // }
        //
        // PaymentTransactionModel paymentTransaction = paymentTransactionRepository
        // .findPaymentTransactionByRequestReferenceNumber(booking.getRequestReferenceNumber())
        // .orElseThrow(() -> new PaymentTransactionNotFoundException(
        // String.format("Payment transaction with reference number '%s' not found.",
        // booking.requestReferenceNumber())));

        // paymentTransaction.setStatus(PaymentStatus.ONLINE_PREAUTH_PENDING);
        // paymentTransaction.setExpiresAt(paymentTransaction.getPaymentInitiatedAt().plusHours(1).plusMinutes(30));

        return "Pre-authorization done.";
    }

}
