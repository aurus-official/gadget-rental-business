package com.gadget.rental.payment;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionRepository extends CrudRepository<PaymentTransactionModel, Long> {
    @Query("SELECT pTInfo FROM paymentTransactionInfo pTInfo WHERE pTInfo.checkoutId=?1")
    Optional<PaymentTransactionModel> findPaymentTransactionByCheckoutId(String checkoutId);

}
