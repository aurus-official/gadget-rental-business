package com.gadget.rental.auth.jwt;

import jakarta.persistence.AttributeConverter;

public class JwtRefreshTokenStatusConverter implements AttributeConverter<JwtRefreshTokenStatus, String> {

    @Override
    public String convertToDatabaseColumn(JwtRefreshTokenStatus status) {
        return status.value;
    }

    @Override
    public JwtRefreshTokenStatus convertToEntityAttribute(String dbData) {
        return JwtRefreshTokenStatus.valueOf(JwtRefreshTokenStatus.class, dbData.toUpperCase());
    }

}
