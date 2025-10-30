package com.gadget.rental.rental;

import jakarta.persistence.AttributeConverter;

public class RentalGadgetStatusConverter implements AttributeConverter<RentalGadgetStatus, String> {

    @Override
    public String convertToDatabaseColumn(RentalGadgetStatus attribute) {
        return attribute.value.toUpperCase();
    }

    @Override
    public RentalGadgetStatus convertToEntityAttribute(String dbData) {
        return RentalGadgetStatus.valueOf(RentalGadgetStatus.class, dbData.toUpperCase());
    }

}
