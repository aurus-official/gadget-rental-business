package com.gadget.rental.payment.cash;

import jakarta.validation.Valid;

import com.gadget.rental.payment.PaymentTransactionResponseDTO;

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
public class CashPaymentController {

    private final CashPaymentService cashPaymentService;

    CashPaymentController(CashPaymentService cashPaymentService) {
        this.cashPaymentService = cashPaymentService;
    }

    @PostMapping(path = "/cash-payments")
    ResponseEntity<String> createCashPayment(@Valid @RequestBody CashPaymentDetailsDTO cashPaymentDetailsDTO) {
        String message = cashPaymentService.createCashPaymentForBooking(cashPaymentDetailsDTO);
        return ResponseEntity.ok(message);
    }

    @GetMapping(path = "/cash-payments/{requestReferenceNumber}")
    ResponseEntity<PaymentTransactionResponseDTO> getCashPayment(
            @PathVariable("requestReferenceNumber") String requestReferenceNumber) {
        PaymentTransactionResponseDTO paymentTransactionResponseDTO = cashPaymentService
                .getCashPaymentForBooking(requestReferenceNumber);
        return ResponseEntity.ok(paymentTransactionResponseDTO);
    }

    @DeleteMapping(path = "/cash-payments/{requestReferenceNumber}")
    ResponseEntity<String> cancelCashPayment(
            @PathVariable("requestReferenceNumber") String requestReferenceNumber) {
        String message = cashPaymentService.cancelCashPaymentForBooking(requestReferenceNumber);
        return ResponseEntity.ok(message);
    }

    @PostMapping(path = "/cash-deposit/{requestReferenceNumber}")
    ResponseEntity<String> createCashDeposit(@RequestBody CashDepositDetailsDTO cashDepositDetailsDTO,
            @PathVariable("requestReferenceNumber") String requestReferenceNumber) {
        String message = cashPaymentService.createCashDepositForRestrictedFunds(cashDepositDetailsDTO,
                requestReferenceNumber);
        return ResponseEntity.ok(message);
    }
}
