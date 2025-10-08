package com.gadget.rental.auth.jwt;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.gadget.rental.shared.AccountType;
import com.gadget.rental.shared.AccountTypeConverter;

@Entity(name = "refreshTokenInfo")
@Table(name = "refreshTokenInfo")
public class JwtRefreshTokenModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "token", length = 512)
    private String token;

    @Column(name = "valid_until")
    private ZonedDateTime validUntil;

    @Column(name = "valid_from")
    private ZonedDateTime validFrom;

    @Convert(converter = JwtRefreshTokenStatusConverter.class)
    @Column(name = "status")
    private JwtRefreshTokenStatus status;

    @Convert(converter = AccountTypeConverter.class)
    private AccountType accountType;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ZonedDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(ZonedDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public ZonedDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(ZonedDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public JwtRefreshTokenStatus getStatus() {
        return status;
    }

    public void setStatus(JwtRefreshTokenStatus status) {
        this.status = status;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

}
