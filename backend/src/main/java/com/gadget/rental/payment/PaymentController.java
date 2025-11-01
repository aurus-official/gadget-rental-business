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
            @Valid @PathVariable("requestTransaction") PaymentRequestTransactionDTO requestTransactionDTO) {
        String message = paymentService.getOnlinePaymentForBooking(requestTransactionDTO);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping(path = "/online-payments/{checkoutId}")
    ResponseEntity<String> cancelOnlinePayment(
            @Valid @PathVariable("checkoutId") PaymentRequestTransactionDTO requestTransactionDTO) {
        String message = paymentService.cancelOnlinePaymentForBooking(requestTransactionDTO);
        return ResponseEntity.ok(message);
    }

    @PostMapping(path = "/cash-payments")
    ResponseEntity<String> createCashPayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        String message = paymentService.createCashPaymentForBooking(paymentDTO);
        return ResponseEntity.ok(message);
    }

    @GetMapping(path = "/cash-payments/{checkoutId}")
    ResponseEntity<String> getCashPayment(
            @Valid @PathVariable("checkoutId") PaymentRequestTransactionDTO requestTransactionDTO) {
        String message = paymentService.getCashPaymentForBooking(requestTransactionDTO);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping(path = "/cash-payments/{checkoutId}")
    ResponseEntity<String> cancelCashPayment(
            @Valid @PathVariable("checkoutId") PaymentRequestTransactionDTO requestTransactionDTO) {
        String message = paymentService.cancelCashPaymentForBooking(requestTransactionDTO);
        return ResponseEntity.ok(message);
    }
}
