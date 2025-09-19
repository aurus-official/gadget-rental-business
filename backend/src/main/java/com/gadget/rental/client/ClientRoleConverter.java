package com.gadget.rental.client;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ClientRoleConverter implements AttributeConverter<List<ClientRole>, String> {
    private static final String SPLIT_CHAR = ", ";

    @Override
    public String convertToDatabaseColumn(List<ClientRole> attribute) {
        return attribute.stream().map(role -> role.value).collect(Collectors.joining(SPLIT_CHAR));
    }

    @Override
    public List<ClientRole> convertToEntityAttribute(String dbData) {
        return Stream.of(dbData.split(SPLIT_CHAR)).map(role -> {
            return ClientRole.valueOf(role.toUpperCase());
        }).collect(Collectors.toList());
    }

}
