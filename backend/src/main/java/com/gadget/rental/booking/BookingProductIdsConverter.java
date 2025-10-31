package com.gadget.rental.booking;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.AttributeConverter;

public class BookingProductIdsConverter implements AttributeConverter<List<Long>, String> {

    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {
        return attribute.stream().map(String::valueOf).collect(Collectors.joining(", "));
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        return Arrays.stream(dbData.split(", ")).map(Long::valueOf).collect(Collectors.toList());
    }

}
