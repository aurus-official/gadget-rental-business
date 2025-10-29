package com.gadget.rental.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class PaymentWebhookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentWebhookController.class);

    @Async
    @PostMapping(path = "/webhooks/payment")
    ResponseEntity<String> handlePaymentWebhook(@RequestBody PaymentWebhookContent paymentWebhookContent) {
        LOGGER.info(paymentWebhookContent.toString());
        switch (PaymentStatus.valueOf(paymentWebhookContent.getStatus())) {
            case PAYMENT_SUCCESS:
                System.out.println("Payment Success");
                break;
            case PAYMENT_FAILED:
                System.out.println("Payment Failed");
                break;
            case PAYMENT_EXPIRED:
                System.out.println("Payment Expired");
                break;
            case PAYMENT_CANCELLED:
                System.out.println("Payment Cancelled");
                break;
            case AUTHORIZED:
                System.out.println("Payment Authorized");
                break;
            case AUTH_FAILED:
                System.out.println("Payment Auth Failed");
                break;
        }

        return ResponseEntity.status(HttpStatus.OK).body("Received!");
    }
}
