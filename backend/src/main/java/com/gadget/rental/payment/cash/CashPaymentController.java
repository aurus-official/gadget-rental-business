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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class CashPaymentController {

    private final CashPaymentService cashPaymentService;

    CashPaymentController(CashPaymentService cashPaymentService) {
        this.cashPaymentService = cashPaymentService;
    }

    //
    // TODO: Change cashpaymentDto for createCashPayment and createCashDeposit
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
            @PathVariable("requestReferenceNumber") String requestReferenceNumber,
            @RequestParam("email") String email) {

        @Valid
        CashPaymentTransactionRequestDTO cashPaymentTransactionRequestDTO = new CashPaymentTransactionRequestDTO(
                checkoutId, requestReferenceNumber, email);
        PaymentTransactionHistoryResponseDTO paymentTransactionResponseDTO = cashPaymentService
                .getCashPaymentForBooking(cashPaymentTransactionRequestDTO);

        return ResponseEntity.ok(paymentTransactionResponseDTO);
    }

    @DeleteMapping(path = "/cash-payments/{requestReferenceNumber}/checkouts/{checkoutId}")
    ResponseEntity<String> cancelCashPayment(
            @PathVariable("checkoutId") String checkoutId,
            @PathVariable("requestReferenceNumber") String requestReferenceNumber,
            @RequestParam("email") String email) {

        @Valid
        CashPaymentTransactionRequestDTO cashPaymentTransactionRequestDTO = new CashPaymentTransactionRequestDTO(
                checkoutId, requestReferenceNumber, email);
        String message = cashPaymentService.cancelCashPaymentForBooking(cashPaymentTransactionRequestDTO);

        return ResponseEntity.ok(message);
    }

    @PostMapping(path = "/cash-deposit/{requestReferenceNumber}")
    ResponseEntity<CashPaymentReponseDTO> createCashDeposit(@RequestBody CashDepositDetailsDTO cashDepositDetailsDTO,
            @PathVariable("requestReferenceNumber") String requestReferenceNumber) {

        CashPaymentReponseDTO cashPaymentReponseDTO = cashPaymentService.createCashDepositForRestrictedFunds(
                cashDepositDetailsDTO,
                requestReferenceNumber);

        return ResponseEntity.ok(cashPaymentReponseDTO);
    }
}
