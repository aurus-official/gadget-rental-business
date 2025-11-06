package com.gadget.rental.payment.online;

import org.hibernate.validator.constraints.UUID;

public record OnlinePaymentTransactionRequestDTO(
        @UUID String checkoutId,
        @UUID String requestReferenceNumber
) {

}
