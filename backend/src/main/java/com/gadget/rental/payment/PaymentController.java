package com.gadget.rental.payment;

import jakarta.validation.Valid;

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

    @GetMapping(path = "/online-payments/{requestReferenceNumber}")
    ResponseEntity<String> getOnlinePayment(
            @PathVariable("requestReferenceNumber") String requestReferenceNumber, @RequestParam String email) {
        @Valid
        PaymentTransactionRequestDTO transactionRequestDTO = new PaymentTransactionRequestDTO(requestReferenceNumber,
                email);
        String message = paymentService.getOnlinePaymentForBooking(transactionRequestDTO);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping(path = "/online-payments/{requestReferenceNumber}")
    ResponseEntity<String> cancelOnlinePayment(
            @PathVariable("requestReferenceNumber") String requestReferenceNumber, @RequestParam String email) {
        @Valid
        PaymentTransactionRequestDTO transactionRequestDTO = new PaymentTransactionRequestDTO(requestReferenceNumber,
                email);
        String message = paymentService.cancelOnlinePaymentForBooking(transactionRequestDTO);
        return ResponseEntity.ok(message);
    }

    @PostMapping(path = "/cash-payments")
    ResponseEntity<String> createCashPayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        String message = paymentService.createCashPaymentForBooking(paymentDTO);
        return ResponseEntity.ok(message);
    }

    @GetMapping(path = "/cash-payments/{requestReferenceNumber}")
    ResponseEntity<PaymentTransactionResponseDTO> getCashPayment(
            @PathVariable("requestReferenceNumber") String requestReferenceNumber, @RequestParam String email) {
        @Valid
        PaymentTransactionRequestDTO transactionRequestDTO = new PaymentTransactionRequestDTO(requestReferenceNumber,
                email);
        PaymentTransactionResponseDTO paymentTransactionResponseDTO = paymentService
                .getCashPaymentForBooking(transactionRequestDTO);
        return ResponseEntity.ok(paymentTransactionResponseDTO);
    }

    @DeleteMapping(path = "/cash-payments/{requestReferenceNumber}")
    ResponseEntity<String> cancelCashPayment(
            @PathVariable("requestReferenceNumber") String requestReferenceNumber,
            @RequestParam("email") String email) {
        @Valid
        PaymentTransactionRequestDTO transactionRequestDTO = new PaymentTransactionRequestDTO(requestReferenceNumber,
                email);
        String message = paymentService.cancelCashPaymentForBooking(transactionRequestDTO);
        return ResponseEntity.ok(message);
    }
}
