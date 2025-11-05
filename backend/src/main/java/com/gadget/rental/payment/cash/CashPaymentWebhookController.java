package com.gadget.rental.payment.cash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class CashPaymentWebhookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CashPaymentWebhookController.class);
    private final CashPaymentWebhookService cashPaymentWebhookService;

    @Autowired
    CashPaymentWebhookController(CashPaymentWebhookService cashPaymentWebhookService) {
        this.cashPaymentWebhookService = cashPaymentWebhookService;
    }

    @Async
    @PostMapping(path = "/webhooks/cash-payment")
    ResponseEntity<String> handleCashPaymentWebhook(
            @RequestBody CashPaymentWebhookPayloadRequestDTO cashPaymentWebhookPayloadRequestDTO) {
        LOGGER.info(cashPaymentWebhookPayloadRequestDTO.toString());
        String message = cashPaymentWebhookService
                .addSuccessfulCashPaymentToTransactions(cashPaymentWebhookPayloadRequestDTO);

        return ResponseEntity.ok(message);
    }

    @Async
    @PostMapping(path = "/webhooks/cash-deposit")
    ResponseEntity<String> handleCashDepositWebhook(
            @RequestBody CashPaymentWebhookPayloadRequestDTO cashPaymentWebhookPayloadRequestDTO) {
        LOGGER.info(cashPaymentWebhookPayloadRequestDTO.toString());
        String message = cashPaymentWebhookService
                .addSuccessfulCashDepositToTransactions(cashPaymentWebhookPayloadRequestDTO);

        return ResponseEntity.ok(message);
    }

}
