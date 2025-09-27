package com.gadget.rental.auth.verification;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity(name = "emailVerificationInfo")
@Table(name = "emailVerificationInfo")
public class EmailVerificationModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "verification_code")
    private String code;

    @Column(name = "expiry_date")
    private ZonedDateTime expiry;

    @Column(name = "next_valid_code_resend_date")
    private ZonedDateTime nextValidCodeResendDate;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "token_account_creation")
    private String tokenAccountCreation;

    @Column(name = "is_verified", columnDefinition = "boolean default false")
    private boolean isVerified;

    @Column(name = "is_linked", columnDefinition = "boolean default false")
    private boolean isLinked;

    @Column(name = "attempt_count")
    private int attemptCount;

    @Convert(converter = EmailVerificationTypeConverter.class)
    @Column(name = "account_type")
    private EmailVerificationType accountType;

    @Transient
    private final static int MAX_ATTEMPT = 5;

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

    public ZonedDateTime getNextValidCodeResendDate() {
        return nextValidCodeResendDate;
    }

    public void setNextValidCodeResendDate(ZonedDateTime nextValidCodeResendDate) {
        this.nextValidCodeResendDate = nextValidCodeResendDate;
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

    public boolean isLinked() {
        return isLinked;
    }

    public void setLinked(boolean isLinked) {
        this.isLinked = isLinked;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(int attemptCount) {
        this.attemptCount = attemptCount;
    }

    public EmailVerificationType getAccountType() {
        return accountType;
    }

    public void setAccountType(EmailVerificationType accountType) {
        this.accountType = accountType;
    }

    public boolean isTimeExpired() {
        return ZonedDateTime.now(ZoneId.of("Z"))
                .withZoneSameInstant(ZoneId.of(this.timezone))
                .isAfter(this.expiry.withZoneSameInstant(ZoneId.of(this.timezone)));
    }

    public boolean isAttemptsExceeded() {
        return this.attemptCount >= MAX_ATTEMPT;
    }

    public boolean isAdminAllowed() {
        return ZonedDateTime.now(ZoneId.of("Z"))
                .withZoneSameInstant(ZoneId.of(this.timezone))
                .isAfter(this.expiry.withZoneSameInstant(ZoneId.of(this.timezone)));
    }

    public boolean isClientReRegistrationCooldownExpired() {
        return ZonedDateTime.now(ZoneId.of("Z"))
                .isAfter(this.expiry.withZoneSameInstant(ZoneId.of(this.timezone)).plusMinutes(15l));
    }

    public boolean isAdminReRegistrationCooldownExpired() {
        return ZonedDateTime.now(ZoneId.of("Z"))
                .isAfter(this.expiry.withZoneSameInstant(ZoneId.of(this.timezone)).plusHours(24l));
    }

    public boolean isEmailResendCooldownExpired() {
        return ZonedDateTime.now(ZoneId.of(this.timezone))
                .isAfter(this.nextValidCodeResendDate.withZoneSameInstant(ZoneId.of(this.timezone)));
    }

    public boolean isVerificationCodeMatched(String code) {
        return this.code.compareTo(code) == 0;
    }

    public boolean isAccountTypeMatched(EmailVerificationType type) {
        return this.accountType == type;
    }

    public boolean isAuthTokenMatched(String authHeader) {

        if (authHeader == null || authHeader.startsWith("Bearer")) {
        }
        String authToken = authHeader.substring(7);
        return this.tokenAccountCreation.compareTo(authToken) == 0;
    }
}
