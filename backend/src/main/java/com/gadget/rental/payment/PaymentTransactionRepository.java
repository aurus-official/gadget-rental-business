package com.gadget.rental.payment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionRepository extends CrudRepository<PaymentTransactionModel, Long> {

}
