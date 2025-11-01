package com.gadget.rental.payment;

import org.hibernate.validator.constraints.UUID;

public record PaymentRequestTransactionDTO(
        @UUID String requestTransactionNumber) {

}
