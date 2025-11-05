package com.gadget.rental.payment.online;

import jakarta.validation.Valid;

import com.gadget.rental.payment.PaymentTransactionRequestDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class OnlinePaymentController {

    private final OnlinePaymentService onlinePaymentService;

    OnlinePaymentController(OnlinePaymentService onlinePaymentService) {
        this.onlinePaymentService = onlinePaymentService;
    }

    @PostMapping(path = "/online-payments")
    ResponseEntity<String> createOnlinePayment(@Valid @RequestBody OnlinePaymentDetailsDTO onlinePaymentDetailsDTO) {
        String message = onlinePaymentService.createOnlinePaymentForBooking(onlinePaymentDetailsDTO);
        return ResponseEntity.ok(message);
    }

    @GetMapping(path = "/online-payments/{requestReferenceNumber}")
    ResponseEntity<String> getOnlinePayment(
            @PathVariable("requestReferenceNumber") String requestReferenceNumber, @RequestParam String email) {
        @Valid
        PaymentTransactionRequestDTO transactionRequestDTO = new PaymentTransactionRequestDTO(requestReferenceNumber,
                email);
        String message = onlinePaymentService.getOnlinePaymentForBooking(transactionRequestDTO);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping(path = "/online-payments/{requestReferenceNumber}")
    ResponseEntity<String> cancelOnlinePayment(
            @PathVariable("requestReferenceNumber") String requestReferenceNumber, @RequestParam String email) {
        @Valid
        PaymentTransactionRequestDTO transactionRequestDTO = new PaymentTransactionRequestDTO(requestReferenceNumber,
                email);
        String message = onlinePaymentService.cancelOnlinePaymentForBooking(transactionRequestDTO);
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
