package com.gadget.rental.payment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionRepository extends CrudRepository<PaymentTransactionModel, Long> {
    @Query("SELECT pTInfo FROM paymentTransactionInfo pTInfo WHERE pTInfo.requestReferenceNumber=?1")
    List<PaymentTransactionModel> findAllPaymentTransactionsByRequestReferenceNumber(String requestReferenceNumber);

    @Query("SELECT pTInfo FROM paymentTransactionInfo pTInfo WHERE pTInfo.checkoutId=?1")
    Optional<PaymentTransactionModel> findPaymentTransactionByCheckoutId(String checkoutId);

    @Query("SELECT pTInfo FROM paymentTransactionInfo pTInfo WHERE " +
            "(pTInfo.expiresAt <= NOW()) AND " +
            "(pTInfo.status != 'cash_payment_cancelled') AND (pTInfo.status != 'cash_deposit_captured') AND " +
            "(pTInfo.status != 'cash_deposit_voided') AND (pTInfo.status != 'cash_deposit_cancelled') AND " +
            "(pTInfo.status != 'online_payment_cancelled') AND (pTInfo.status != 'online_preauth_captured') AND " +
            "(pTInfo.status != 'online_preauth_voided') AND (pTInfo.status != 'online_preauth_cancelled')")
    List<PaymentTransactionModel> findAllExpiredPaymentTransactions();
}
