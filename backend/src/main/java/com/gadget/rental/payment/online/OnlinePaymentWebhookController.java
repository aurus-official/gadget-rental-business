package com.gadget.rental.payment.online;

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
public class OnlinePaymentWebhookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnlinePaymentWebhookController.class);
    private final OnlinePaymentWebhookService onlinePaymentWebhookService;

    @Autowired
    OnlinePaymentWebhookController(OnlinePaymentWebhookService onlinePaymentWebhookService) {
        this.onlinePaymentWebhookService = onlinePaymentWebhookService;
    }

    @Async
    // @PostMapping(path = "/webhooks/payment")
    @PostMapping(path = "/webhooks/online-payment")
    ResponseEntity<String> handleOnlinePaymentWebhook(
            @RequestBody OnlinePaymentWebhookPayloadRequestDTO onlinePaymentWebhookPayloadRequestDTO) {
        LOGGER.info(onlinePaymentWebhookPayloadRequestDTO.toString());
        String message = "";

        switch (OnlinePaymentWebhookStatus.valueOf(onlinePaymentWebhookPayloadRequestDTO.getStatus())) {
            case PAYMENT_PENDING:
                break;
            case PAYMENT_SUCCESS:
                message = onlinePaymentWebhookService
                        .addSuccessfulOnlinePaymentToTransactions(onlinePaymentWebhookPayloadRequestDTO);
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
            case CAPTURED:
                System.out.println("Payment Auth Failed");
                break;
        }

        return ResponseEntity.ok(message);
    }

}
