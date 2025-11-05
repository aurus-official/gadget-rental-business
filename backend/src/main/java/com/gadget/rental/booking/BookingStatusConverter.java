package com.gadget.rental.booking;

import jakarta.persistence.AttributeConverter;

public class BookingStatusConverter implements AttributeConverter<BookingStatus, String> {

    @Override
    public String convertToDatabaseColumn(BookingStatus attribute) {
        return attribute.value.toUpperCase();
    }

    @Override
    public BookingStatus convertToEntityAttribute(String dbData) {
        return BookingStatus.valueOf(BookingStatus.class, dbData.toUpperCase());
    }
}
