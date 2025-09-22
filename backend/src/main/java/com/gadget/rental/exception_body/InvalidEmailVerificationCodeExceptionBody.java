package com.gadget.rental.exception_body;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

public class InvalidEmailVerificationCodeExceptionBody extends BaseExceptionBody {
    public InvalidEmailVerificationCodeExceptionBody(String message, HttpStatus httpStatus,
            ZonedDateTime timeStamp) {
        super.setMessage(message);
        super.setHttpStatus(httpStatus);
        super.setTimeStamp(timeStamp);
    }
}
