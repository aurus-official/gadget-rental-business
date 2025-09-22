package com.gadget.rental.exception_body;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

public class EmailVerificationResendTooSoonExceptionBody extends BaseExceptionBody {
    private String retryAfterSeconds = "60s";

    public String getRetryAfterSeconds() {
        return retryAfterSeconds;
    }

    public void setRetryAfterSeconds(String retryAfterSeconds) {
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public EmailVerificationResendTooSoonExceptionBody(String message, HttpStatus httpStatus,
            ZonedDateTime timeStamp) {
        super.setMessage(message);
        super.setHttpStatus(httpStatus);
        super.setTimeStamp(timeStamp);
    }
}
