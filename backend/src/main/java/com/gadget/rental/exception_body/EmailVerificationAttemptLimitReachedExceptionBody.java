package com.gadget.rental.exception_body;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

public class EmailVerificationAttemptLimitReachedExceptionBody extends BaseExceptionBody {
    public EmailVerificationAttemptLimitReachedExceptionBody(String message, HttpStatus httpStatus,
            ZonedDateTime timeStamp) {
        super.setMessage(message);
        super.setHttpStatus(httpStatus);
        super.setTimeStamp(timeStamp);
    }

}
