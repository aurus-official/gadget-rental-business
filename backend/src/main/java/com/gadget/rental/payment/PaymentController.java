package com.gadget.rental.payment;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class PaymentController {

    private final PaymentService paymentService;

    PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping(path = "/payments/{requestReferenceNumber}")
    ResponseEntity<List<PaymentTransactionHistoryResponseDTO>> getAllPaymentTrasactionsByRequestReferenceNumber(
            @PathVariable("requestReferenceNumber") String requestReferenceNumber,
            @PathVariable String email) {

        @Valid
        PaymentTransactionHistoryRequestDTO paymentTransactionHistoryRequestDTO = new PaymentTransactionHistoryRequestDTO(
                requestReferenceNumber);

        List<PaymentTransactionHistoryResponseDTO> paymentTransactionHistoryResponseDTOs = paymentService
                .getAllPaymentTransactionsHistoryByRequestReferenceNumber(paymentTransactionHistoryRequestDTO);

        return ResponseEntity.ok(paymentTransactionHistoryResponseDTOs);
    }

}
