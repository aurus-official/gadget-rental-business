package com.gadget.rental.account.verification;

import jakarta.persistence.AttributeConverter;

public class EmailVerificationTypeConverter implements AttributeConverter<EmailVerificationType, String> {

    @Override
    public String convertToDatabaseColumn(EmailVerificationType attribute) {
        return attribute.value;
    }

    @Override
    public EmailVerificationType convertToEntityAttribute(String dbData) {
        return EmailVerificationType.valueOf(EmailVerificationType.class, dbData.toUpperCase());
    }

}
