package com.gadget.rental.exception_body;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

public class UsernameDuplicateExceptionBody extends BaseExceptionBody {
    public UsernameDuplicateExceptionBody(String message, HttpStatus httpStatus, ZonedDateTime timeStamp) {
        super.setMessage(message);
        super.setHttpStatus(httpStatus);
        super.setTimeStamp(timeStamp);
    }
}
