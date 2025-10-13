package com.gadget.rental.shared;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

public class ErrorMessageBodyUtil {
    public static String generateErrorMessageBody(String errorMessage, HttpStatus status) {
        ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("Z"));
        String message = "";

        message = String.format(
                "{\n    \"message\" : \"%s\",\n    \"httpStatus\" : \"%s\",\n    \"timeStamp\" : \"%s\"\n}",
                errorMessage,
                status.toString(), timestamp.toString());

        return message;
    }
}
