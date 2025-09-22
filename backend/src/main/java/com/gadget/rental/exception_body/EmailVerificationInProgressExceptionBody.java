package com.gadget.rental.exception_body;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

public class EmailVerificationInProgressExceptionBody extends BaseExceptionBody {
    public EmailVerificationInProgressExceptionBody(String message, HttpStatus httpStatus,
            ZonedDateTime timeStamp) {
        super.setMessage(message);
        super.setHttpStatus(httpStatus);
        super.setTimeStamp(timeStamp);
    }
}
