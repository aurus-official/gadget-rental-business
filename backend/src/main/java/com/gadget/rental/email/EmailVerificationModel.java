package com.gadget.rental.email;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "emailVerificationInfo")
@Table(name = "emailVerificationInfo")
public class EmailVerificationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "verification_code")
    private String code;

    @Column(name = "expiry_date")
    private ZonedDateTime expiry;

    @Column(name = "next_valid_code_resent_date")
    private ZonedDateTime nextValidCodeResentDate;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "token_account_creation")
    private String tokenAccountCreation;

    @Column(name = "is_verified", columnDefinition = "boolean default false")
    private boolean isVerified;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ZonedDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(ZonedDateTime expiry) {
        this.expiry = expiry;
    }

    public ZonedDateTime getNextValidCodeResentDate() {
        return nextValidCodeResentDate;
    }

    public void setNextValidCodeResentDate(ZonedDateTime nextValidCodeResentDate) {
        this.nextValidCodeResentDate = nextValidCodeResentDate;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTokenAccountCreation() {
        return tokenAccountCreation;
    }

    public void setTokenAccountCreation(String tokenAccountCreation) {
        this.tokenAccountCreation = tokenAccountCreation;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }
}
