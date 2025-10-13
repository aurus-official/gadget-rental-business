package com.gadget.rental.shared;

import jakarta.persistence.AttributeConverter;

public class AccountTypeConverter implements AttributeConverter<AccountType, String> {

    @Override
    public String convertToDatabaseColumn(AccountType attribute) {
        return attribute.value.toUpperCase();
    }

    @Override
    public AccountType convertToEntityAttribute(String dbData) {
        return AccountType.valueOf(AccountType.class, dbData.toUpperCase());
    }

}
