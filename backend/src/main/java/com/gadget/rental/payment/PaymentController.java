package com.gadget.rental.payment;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class PaymentController {

    private final PaymentService paymentService;

    PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(path = "/payments")
    ResponseEntity<String> createPayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        String message = paymentService.createPaymentForBooking(paymentDTO);
        return ResponseEntity.ok(message);
    }
}
