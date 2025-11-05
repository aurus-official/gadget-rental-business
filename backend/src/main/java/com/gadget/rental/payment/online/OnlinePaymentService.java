package com.gadget.rental.payment.online;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import com.gadget.rental.auth.jwt.JwtAuthenticationToken;
import com.gadget.rental.booking.BookingModel;
import com.gadget.rental.booking.BookingRepository;
import com.gadget.rental.exception.BookingNotFoundException;
import com.gadget.rental.exception.PaymentTransactionNotFoundException;
import com.gadget.rental.exception.RentalGadgetNotFoundException;
import com.gadget.rental.payment.PaymentItem;
import com.gadget.rental.payment.PaymentPayloadRequest;
import com.gadget.rental.payment.PaymentPayloadRequest.Buyer.Contact;
import com.gadget.rental.payment.PaymentStatus;
import com.gadget.rental.payment.PaymentTransactionModel;
import com.gadget.rental.payment.PaymentTransactionRepository;
import com.gadget.rental.payment.PaymentTransactionRequestDTO;
import com.gadget.rental.rental.RentalGadgetModel;
import com.gadget.rental.rental.RentalGadgetRepository;
import com.gadget.rental.rental.RentalGadgetStatus;
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

    OnlinePaymentService(RentalGadgetRepository rentalGadgetRepository, BookingRepository bookingRepository,
            RestTemplate restTemplate, PaymentTransactionRepository paymentTransactionRepository) {
        this.rentalGadgetRepository = rentalGadgetRepository;
        this.bookingRepository = bookingRepository;
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    String createOnlinePaymentForBooking(OnlinePaymentDetailsDTO onlinePaymentDetailsDTO) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) securityContext.getAuthentication();

        List<PaymentItem> itemList = new ArrayList<>();
        double totalPrice = 0.0;

        BookingModel booking = bookingRepository
                .findBookingByRequestReferenceNumber(onlinePaymentDetailsDTO.requestReferenceNumber())
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Booking with reference number '%s' not found.",
                                onlinePaymentDetailsDTO.requestReferenceNumber())));

        for (long id : booking.getRentalGadgetProductIdList()) {
            RentalGadgetModel rentalGadgetModel = rentalGadgetRepository.findById(id)
                    .orElseThrow(() -> new RentalGadgetNotFoundException("Rental gadget is missing."));

            totalPrice += rentalGadgetModel.getPrice();
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

        System.out.println(totalPrice);
        PaymentPayloadRequest paymentPayloadRequest = new PaymentPayloadRequest();
        paymentPayloadRequest.setTotalAmount(new PaymentPayloadRequest.TotalAmount());
        paymentPayloadRequest.getTotalAmount().setValue(totalPrice);
        paymentPayloadRequest.getTotalAmount().setCurrency("PHP");
        paymentPayloadRequest.setBuyer(new PaymentPayloadRequest.Buyer());
        paymentPayloadRequest.getBuyer().setFirstName(onlinePaymentDetailsDTO.firstName());
        paymentPayloadRequest.getBuyer().setLastName(onlinePaymentDetailsDTO.lastName());
        paymentPayloadRequest.getBuyer().setContact(new Contact());
        paymentPayloadRequest.getBuyer().getContact().setEmail(jwtAuthenticationToken.getName());
        paymentPayloadRequest.getBuyer().getContact().setPhone(onlinePaymentDetailsDTO.phoneNumber());
        paymentPayloadRequest.setRequestReferenceNumber(booking.getRequestReferenceNumber());
        paymentPayloadRequest.setItemList(itemList);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization",
                String.format("Basic %s", Base64Util.encodeBase64(String.format("%s:", publicKey).getBytes())));
        HttpEntity<PaymentPayloadRequest> httpEntity = new HttpEntity<>(paymentPayloadRequest, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(mayaCheckoutUrl, httpEntity,
                String.class);

        PaymentTransactionModel paymentTransaction = new PaymentTransactionModel();
        paymentTransaction.setTotalPrice(totalPrice);
        paymentTransaction.setCreatedBy(jwtAuthenticationToken.getName());
        paymentTransaction.setRequestReferenceNumber(booking.getRequestReferenceNumber());
        paymentTransaction.setStatus(PaymentStatus.ONLINE_PAYMENT_PENDING);

        paymentTransactionRepository.save(paymentTransaction);

        return responseEntity.getBody();
    }

    String getOnlinePaymentForBooking(PaymentTransactionRequestDTO paymentTransactionRequestDTO) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) securityContext.getAuthentication();

        // TODO : Check if email in jwtauthtoken matches the returned payment data;

        return "";
    }

    String cancelOnlinePaymentForBooking(PaymentTransactionRequestDTO paymentTransactionRequestDTO) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) securityContext.getAuthentication();

        // TODO : Cancel the online payment through paymentId aka the checkoutId

        return "";
    }

    @Transactional
    String createOnlinePreAuthForRestrictedFunds(PaymentTransactionRequestDTO paymentTransactionRequestDTO) {
        BookingModel booking = bookingRepository
                .findBookingByRequestReferenceNumber(paymentTransactionRequestDTO.requestReferenceNumber())
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Booking with reference number '%s' not found.",
                                paymentTransactionRequestDTO.requestReferenceNumber())));

        for (Long id : booking.getRentalGadgetProductIdList()) {
            RentalGadgetModel rentalGadget = rentalGadgetRepository.findById(id)
                    .orElseThrow(() -> new RentalGadgetNotFoundException("Rental gadget listing is missing."));
            rentalGadget.setStatus(RentalGadgetStatus.AVAILABLE);
        }

        PaymentTransactionModel paymentTransaction = paymentTransactionRepository
                .findPaymentTransactionByRequestReferenceNumber(booking.getRequestReferenceNumber())
                .orElseThrow(() -> new PaymentTransactionNotFoundException(
                        String.format("Payment transaction with reference number '%s' not found.",
                                paymentTransactionRequestDTO.requestReferenceNumber())));

        paymentTransaction.setStatus(PaymentStatus.ONLINE_PREAUTH_PENDING);

        return "Pre-authorization done.";
    }

}
