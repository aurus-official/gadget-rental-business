package com.gadget.rental.auth.verification;

import java.time.ZonedDateTime;

public record EmailVerificationResponseDTO(
        String email,
        ZonedDateTime codeResendAvailableAt,
        ZonedDateTime validUntil) {
}
