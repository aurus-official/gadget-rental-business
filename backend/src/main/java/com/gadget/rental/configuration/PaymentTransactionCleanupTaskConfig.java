package com.gadget.rental.configuration;

import com.gadget.rental.payment.PaymentTransactionCleanupTask;
import com.gadget.rental.payment.PaymentTransactionRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentTransactionCleanupTaskConfig {

    @Bean
    PaymentTransactionCleanupTask getPaymentTransactionCleanupTask(
            PaymentTransactionRepository paymentTransactionRepository) {
        return new PaymentTransactionCleanupTask(paymentTransactionRepository);
    }
}
