package com.gadget.rental.payment.cash;

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
public class CashPaymentController {

    private final CashPaymentService cashPaymentService;

    CashPaymentController(CashPaymentService cashPaymentService) {
        this.cashPaymentService = cashPaymentService;
    }

    @PostMapping(path = "/cash-payments")
    ResponseEntity<CashPaymentReponseDTO> createCashPayment(
            @Valid @RequestBody CashPaymentDetailsDTO cashPaymentDetailsDTO) {
        CashPaymentReponseDTO cashPaymentReponseDTO = cashPaymentService
                .createCashPaymentForBooking(cashPaymentDetailsDTO);
        return ResponseEntity.ok(cashPaymentReponseDTO);
    }

    @GetMapping(path = "/cash-payments/{requestReferenceNumber}/checkouts/{checkoutId}")
    ResponseEntity<PaymentTransactionHistoryResponseDTO> getCashPayment(
            @PathVariable("checkoutId") String checkoutId,
            @PathVariable("requestReferenceNumber") String requestReferenceNumber) {

        @Valid
        CashTransactionRequestDTO cashTransactionRequestDTO = new CashTransactionRequestDTO(
                checkoutId, requestReferenceNumber);
        PaymentTransactionHistoryResponseDTO paymentTransactionResponseDTO = cashPaymentService
                .getCashPaymentForBooking(cashTransactionRequestDTO);

        return ResponseEntity.ok(paymentTransactionResponseDTO);
    }

    @DeleteMapping(path = "/cash-payments/{requestReferenceNumber}/checkouts/{checkoutId}")
    ResponseEntity<String> cancelCashPayment(
            @PathVariable("checkoutId") String checkoutId,
            @PathVariable("requestReferenceNumber") String requestReferenceNumber) {

        @Valid
        CashTransactionRequestDTO cashTransactionRequestDTO = new CashTransactionRequestDTO(
                checkoutId, requestReferenceNumber);
        String message = cashPaymentService.cancelCashPaymentForBooking(cashTransactionRequestDTO);

        return ResponseEntity.ok(message);
    }

    @PostMapping(path = "/cash-deposits")
    ResponseEntity<CashPaymentReponseDTO> createCashDeposit(@RequestBody CashDepositDetailsDTO cashDepositDetailsDTO) {

        CashPaymentReponseDTO cashPaymentReponseDTO = cashPaymentService.createCashDepositForRestrictedFunds(
                cashDepositDetailsDTO);

        return ResponseEntity.ok(cashPaymentReponseDTO);
    }

    @GetMapping(path = "/cash-deposits/{requestReferenceNumber}/checkouts/{checkoutId}")
    ResponseEntity<PaymentTransactionHistoryResponseDTO> getCashDeposit(
            @PathVariable("checkoutId") String checkoutId,
            @PathVariable("requestReferenceNumber") String requestReferenceNumber) {

        @Valid
        CashTransactionRequestDTO cashTransactionRequestDTO = new CashTransactionRequestDTO(
                checkoutId, requestReferenceNumber);
        PaymentTransactionHistoryResponseDTO paymentTransactionResponseDTO = cashPaymentService
                .getCashPaymentForBooking(cashTransactionRequestDTO);

        return ResponseEntity.ok(paymentTransactionResponseDTO);
    }

    @DeleteMapping(path = "/cash-deposits/{requestReferenceNumber}/checkouts/{checkoutId}")
    ResponseEntity<String> cancelCashDeposit(
            @PathVariable("checkoutId") String checkoutId,
            @PathVariable("requestReferenceNumber") String requestReferenceNumber) {

        @Valid
        CashTransactionRequestDTO cashTransactionRequestDTO = new CashTransactionRequestDTO(
                checkoutId, requestReferenceNumber);
        String message = cashPaymentService.cancelCashPaymentForBooking(cashTransactionRequestDTO);

        return ResponseEntity.ok(message);
    }
}
