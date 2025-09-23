package com.gadget.rental.exception_body;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

public class EmailNotVerifiedExceptionBody extends BaseExceptionBody {
    public EmailNotVerifiedExceptionBody(String message, HttpStatus httpStatus, ZonedDateTime timeStamp) {
        super.setMessage(message);
        super.setHttpStatus(httpStatus);
        super.setTimeStamp(timeStamp);
    }

}
