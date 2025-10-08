package com.gadget.rental.auth.jwt;

public enum JwtRefreshTokenStatus {
    ACTIVE("active"), REVOKED("revoked"), EXPIRED("expired");

    String value;

    JwtRefreshTokenStatus(String value) {
        this.value = value;
    }
}
