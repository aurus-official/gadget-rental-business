package com.gadget.rental.payment.online;

import jakarta.validation.Valid;

import com.gadget.rental.payment.PaymentTransactionHistoryResponseDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class OnlinePaymentController {

    private final OnlinePaymentService onlinePaymentService;

    OnlinePaymentController(OnlinePaymentService onlinePaymentService) {
        this.onlinePaymentService = onlinePaymentService;
    }

    @PostMapping(path = "/online-payments")
    ResponseEntity<OnlineCheckoutResponseDTO> createOnlinePayment(
            @Valid @RequestBody OnlinePaymentDetailsDTO onlinePaymentDetailsDTO) {
        OnlineCheckoutResponseDTO onlineCheckoutResponseDTO = onlinePaymentService
                .createOnlinePaymentForBooking(onlinePaymentDetailsDTO);
        return ResponseEntity.ok(onlineCheckoutResponseDTO);
    }

    @GetMapping(path = "/online-payments/{requestReferenceNumber}/checkout/{checkoutId}")
    ResponseEntity<PaymentTransactionHistoryResponseDTO> getOnlinePayment(
            @PathVariable("checkoutId") String checkoutId,
            @PathVariable("requestReferenceNumber") String requestReferenceNumber) {

        @Valid
        OnlinePaymentTransactionRequestDTO onlinePaymentTransactionRequestDTO = new OnlinePaymentTransactionRequestDTO(
                checkoutId, requestReferenceNumber);
        PaymentTransactionHistoryResponseDTO paymentTransactionHistoryResponseDTO = onlinePaymentService
                .getOnlinePaymentForBooking(onlinePaymentTransactionRequestDTO);
        return ResponseEntity.ok(paymentTransactionHistoryResponseDTO);
    }

    @DeleteMapping(path = "/online-payments/{requestReferenceNumber}/checkout/{checkoutId}")
    ResponseEntity<String> cancelOnlinePayment(
            @PathVariable("checkoutId") String checkoutId,
            @PathVariable("requestReferenceNumber") String requestReferenceNumber) {

        @Valid
        OnlinePaymentTransactionRequestDTO onlinePaymentTransactionRequestDTO = new OnlinePaymentTransactionRequestDTO(
                checkoutId, requestReferenceNumber);
        String message = onlinePaymentService.cancelOnlinePaymentForBooking(onlinePaymentTransactionRequestDTO);
        return ResponseEntity.ok(message);
    }

    // TODO: Implement this
    @PostMapping(path = "/online-preauth/{requestReferenceNumber}")
    ResponseEntity<String> createOnlinePreAuth(@Valid @RequestBody OnlinePaymentDetailsDTO onlinePaymentDetailsDTO) {
        // String message = paymentService.createCashPaymentForBooking(paymentDTO);
        String message = "";
        return ResponseEntity.ok(message);
    }

}
