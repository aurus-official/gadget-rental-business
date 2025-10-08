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

import com.gadget.rental.shared.AccountType;
import com.gadget.rental.shared.AccountTypeConverter;

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

    @Column(name = "valid_until")
    private ZonedDateTime validUntil;

    @Column(name = "code_resend_available_at")
    private ZonedDateTime codeResendAvailableAt;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "signup_token")
    private String signupToken;

    @Column(name = "is_verified", columnDefinition = "boolean default false")
    private boolean isVerified;

    @Column(name = "is_linked", columnDefinition = "boolean default false")
    private boolean isLinked;

    @Column(name = "attempt_count")
    private int attemptCount;

    @Convert(converter = AccountTypeConverter.class)
    @Column(name = "account_type")
    private AccountType accountType;

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

    public ZonedDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(ZonedDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public ZonedDateTime getCodeResendAvailableAt() {
        return codeResendAvailableAt;
    }

    public void setCodeResendAvailableAt(ZonedDateTime codeResendAvailableAt) {
        this.codeResendAvailableAt = codeResendAvailableAt;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
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

    public String getSignupToken() {
        return signupToken;
    }

    public void setSignupToken(String signupToken) {
        this.signupToken = signupToken;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(int attemptCount) {
        this.attemptCount = attemptCount;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public boolean isTimeExpired() {
        return ZonedDateTime.now(ZoneId.of("Z"))
                .withZoneSameInstant(ZoneId.of(this.timezone))
                .isAfter(this.validUntil.withZoneSameInstant(ZoneId.of(this.timezone)));
    }

    public boolean isAttemptsExceeded() {
        return this.attemptCount >= MAX_ATTEMPT;
    }

    public boolean isAdminAllowed() {
        return ZonedDateTime.now(ZoneId.of("Z"))
                .withZoneSameInstant(ZoneId.of(this.timezone))
                .isAfter(this.validUntil.withZoneSameInstant(ZoneId.of(this.timezone)));
    }

    public boolean isClientReRegistrationCooldownExpired() {
        return ZonedDateTime.now(ZoneId.of("Z"))
                .isAfter(this.validUntil.withZoneSameInstant(ZoneId.of(this.timezone)).plusMinutes(15l));
    }

    public boolean isAdminReRegistrationCooldownExpired() {
        return ZonedDateTime.now(ZoneId.of("Z"))
                .isAfter(this.validUntil.withZoneSameInstant(ZoneId.of(this.timezone)).plusHours(24l));
    }

    public boolean isEmailResendCooldownExpired() {
        return ZonedDateTime.now(ZoneId.of(this.timezone))
                .isAfter(this.codeResendAvailableAt.withZoneSameInstant(ZoneId.of(this.timezone)));
    }

    public boolean isVerificationCodeMatched(String code) {
        return this.code.compareTo(code) == 0;
    }

    public boolean isAccountTypeMatched(AccountType type) {
        return this.accountType == type;
    }

    public boolean isAuthTokenMatched(String authHeader) {

        if (authHeader == null || authHeader.startsWith("Bearer")) {
        }
        String authToken = authHeader.substring(7);
        return this.signupToken.compareTo(authToken) == 0;
    }
}
