package com.gadget.rental.payment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.gadget.rental.booking.BookingModel;
import com.gadget.rental.booking.BookingRepository;
import com.gadget.rental.exception.RentalGadgetMissingException;
import com.gadget.rental.payment.PaymentRequestPayload.Buyer.Contact;
import com.gadget.rental.rental.RentalGadgetModel;
import com.gadget.rental.rental.RentalGadgetRepository;
import com.gadget.rental.shared.Base64Util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService {

    private final RentalGadgetRepository rentalGadgetRepository;
    private final BookingRepository bookingRepository;
    private final RestTemplate restTemplate;

    @Value("${maya.checkout.sandbox.url}")
    private String mayaCheckoutUrl;

    @Value("${maya.public.key.sandbox}")
    private String publicKey;

    @Value("${maya.secret.key.sandbox}")
    private String secretKey;

    PaymentService(RentalGadgetRepository rentalGadgetRepository, BookingRepository bookingRepository,
            RestTemplate restTemplate) {
        this.rentalGadgetRepository = rentalGadgetRepository;
        this.bookingRepository = bookingRepository;
        this.restTemplate = restTemplate;
    }

    String createPaymentForProducts(PaymentDTO paymentDTO) {
        System.out.println(publicKey);
        List<PaymentItem> itemList = new ArrayList<>();
        double totalPrice = 0.0;

        BookingModel booking = bookingRepository.findBookingByReferenceNumber(paymentDTO.referenceNumber())
                .orElseThrow(() -> new RuntimeException());

        for (int id : booking.getRentalGadgetProductIdList()) {
            RentalGadgetModel rentalGadgetModel = rentalGadgetRepository.findById(Long.valueOf(id))
                    .orElseThrow(() -> new RentalGadgetMissingException("Rental gadget is missing."));

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
        PaymentRequestPayload paymentRequestPayload = new PaymentRequestPayload();
        paymentRequestPayload.setTotalAmount(new PaymentRequestPayload.TotalAmount());
        paymentRequestPayload.getTotalAmount().setValue(totalPrice);
        paymentRequestPayload.getTotalAmount().setCurrency("PHP");
        paymentRequestPayload.setBuyer(new PaymentRequestPayload.Buyer());
        paymentRequestPayload.getBuyer().setFirstName(paymentDTO.firstName());
        paymentRequestPayload.getBuyer().setLastName(paymentDTO.lastName());
        paymentRequestPayload.getBuyer().setContact(new Contact());
        paymentRequestPayload.getBuyer().getContact().setEmail(paymentDTO.email());
        paymentRequestPayload.getBuyer().getContact().setPhone(paymentDTO.phoneNumber());
        paymentRequestPayload.setRequestReferenceNumber(UUID.randomUUID().toString());
        paymentRequestPayload.setItemList(itemList);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization",
                String.format("Basic %s", Base64Util.encodeBase64(String.format("%s:", publicKey).getBytes())));
        HttpEntity<PaymentRequestPayload> httpEntity = new HttpEntity<>(paymentRequestPayload, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(mayaCheckoutUrl, httpEntity,
                String.class);

        return responseEntity.getBody();
    }

}
