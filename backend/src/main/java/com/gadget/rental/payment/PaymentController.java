package com.gadget.rental.payment;

import jakarta.validation.Valid;

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
public class PaymentController {

    private final PaymentService paymentService;

    PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(path = "/online-payments")
    ResponseEntity<String> createOnlinePayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        String message = paymentService.createOnlinePaymentForBooking(paymentDTO);
        return ResponseEntity.ok(message);
    }

    @GetMapping(path = "/online-payments/{requestTransaction}")
    ResponseEntity<String> getOnlinePayment(
            @Valid @PathVariable("requestTransaction") PaymentTransactionRequestDTO transactionRequestDTO) {
        String message = paymentService.getOnlinePaymentForBooking(transactionRequestDTO);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping(path = "/online-payments/{requestTransaction}")
    ResponseEntity<String> cancelOnlinePayment(
            @Valid @PathVariable("requestTransaction") PaymentTransactionRequestDTO transactionRequestDTO) {
        String message = paymentService.cancelOnlinePaymentForBooking(transactionRequestDTO);
        return ResponseEntity.ok(message);
    }

    @PostMapping(path = "/cash-payments")
    ResponseEntity<String> createCashPayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        String message = paymentService.createCashPaymentForBooking(paymentDTO);
        return ResponseEntity.ok(message);
    }

    @GetMapping(path = "/cash-payments/{checkoutId}")
    ResponseEntity<String> getCashPayment(
            @Valid @PathVariable("checkoutId") PaymentTransactionRequestDTO transactionRequestDTO) {
        String message = paymentService.getCashPaymentForBooking(transactionRequestDTO);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping(path = "/cash-payments/{requestTransaction}")
    ResponseEntity<String> cancelCashPayment(
            @Valid @PathVariable("requestTransaction") PaymentTransactionRequestDTO transactionRequestDTO) {
        String message = paymentService.cancelCashPaymentForBooking(transactionRequestDTO);
        return ResponseEntity.ok(message);
    }
}
