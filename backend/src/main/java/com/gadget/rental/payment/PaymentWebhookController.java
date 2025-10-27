package com.gadget.rental.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class PaymentWebhookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentWebhookController.class);

    @PostMapping(path = "/webhooks/payment")
    ResponseEntity<String> handlePaymentWebhook(@RequestBody PaymentWebhookContent paymentWebhookContent) {
        LOGGER.info(paymentWebhookContent.toString());

        return ResponseEntity.status(HttpStatus.OK).body("Received!");
    }
}
