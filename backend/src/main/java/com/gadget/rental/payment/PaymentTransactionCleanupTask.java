package com.gadget.rental.payment;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;

public class PaymentTransactionCleanupTask {

    private PaymentTransactionRepository paymentTransactionRepository;

    public PaymentTransactionCleanupTask(PaymentTransactionRepository paymentTransactionRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    @Scheduled(cron = "0 0 0 1 1/6 ?")
    void deleteExpiredPayment() {
        List<PaymentTransactionModel> expiredPaymentTransactions = paymentTransactionRepository
                .findAllExpiredPaymentTransactions();

        if (expiredPaymentTransactions.size() == 0) {
            return;
        }

        expiredPaymentTransactions.stream().forEach((transaction) -> {
            paymentTransactionRepository.delete(transaction);
        });

    }
}
