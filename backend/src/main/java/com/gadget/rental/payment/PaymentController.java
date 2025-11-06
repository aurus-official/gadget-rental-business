package com.gadget.rental.payment;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class PaymentController {

    private final PaymentService paymentService;

    PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping(path = "/booking/{requestReferenceNumber}")
    ResponseEntity<List<PaymentTransactionHistoryResponseDTO>> getAllPaymentTrasactionsByRequestReferenceNumber() {
        return ResponseEntity.ok(Collections.emptyList());
    }
}
