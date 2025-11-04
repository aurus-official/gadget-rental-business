package com.gadget.rental.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final PaymentWebhookService paymentWebhookService;

    @Autowired
    PaymentWebhookController(PaymentWebhookService paymentWebhookService) {
        this.paymentWebhookService = paymentWebhookService;
    }

    @Async
    // @PostMapping(path = "/webhooks/payment")
    @PostMapping(path = "/webhooks/online-payment")
    ResponseEntity<String> handleOnlinePaymentWebhook(
            @RequestBody OnlinePaymentWebhookPayloadRequestDTO onlinePaymentWebhookPayloadRequestDTO) {
        LOGGER.info(onlinePaymentWebhookPayloadRequestDTO.toString());
        switch (PaymentStatus.valueOf(onlinePaymentWebhookPayloadRequestDTO.getStatus())) {
            case PAYMENT_PENDING:
                break;
            case PAYMENT_SUCCESS:
                paymentWebhookService.addSuccessfulOnlinePaymentToTransactions(onlinePaymentWebhookPayloadRequestDTO);
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

    @Async
    @PostMapping(path = "/webhooks/cash-payment")
    ResponseEntity<String> handleCashPaymentWebhook(
            @RequestBody CashPaymentWebhookPayloadRequestDTO cashPaymentWebhookPayloadRequestDTO) {
        LOGGER.info(cashPaymentWebhookPayloadRequestDTO.toString());
        switch (PaymentStatus.valueOf(cashPaymentWebhookPayloadRequestDTO.getStatus())) {
            case PAYMENT_PENDING:
                break;
            case PAYMENT_SUCCESS:
                paymentWebhookService.addSuccessfulCashPaymentToTransactions(cashPaymentWebhookPayloadRequestDTO);
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
