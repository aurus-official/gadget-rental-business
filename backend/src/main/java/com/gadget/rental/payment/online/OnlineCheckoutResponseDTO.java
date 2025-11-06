package com.gadget.rental.payment.online;

public record OnlineCheckoutResponseDTO(
        String referenceNumber,
        String checkoutID,
        String redirectUrl,
        String createdBy) {
}
