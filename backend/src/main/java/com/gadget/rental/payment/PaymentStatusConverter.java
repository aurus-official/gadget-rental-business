package com.gadget.rental.payment;

import jakarta.persistence.AttributeConverter;

public class PaymentStatusConverter implements AttributeConverter<PaymentStatus, String> {

    @Override
    public String convertToDatabaseColumn(PaymentStatus attribute) {
        return attribute.value.toUpperCase();
    }

    @Override
    public PaymentStatus convertToEntityAttribute(String dbData) {
        return PaymentStatus.valueOf(PaymentStatus.class, dbData.toUpperCase());
    }

}
