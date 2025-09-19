package com.gadget.rental.exception_handler;

import com.gadget.rental.exception.ClientExistedException;
import com.gadget.rental.exception.EmailVerificationFailedException;
import com.gadget.rental.exception_body.ClientExistedExceptionBody;
import com.gadget.rental.exception_body.EmailVerificationFailedExceptionBody;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { ClientExistedException.class })
    public ResponseEntity<ClientExistedExceptionBody> handleClientExistedException(Exception e) {
        ClientExistedExceptionBody exceptionBody = new ClientExistedExceptionBody(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z")));
        return ResponseEntity.badRequest().body(exceptionBody);
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        Map<String, String> errorMap = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap((error) -> error.getField(),
                        (error) -> error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errorMap);
    }

    @ExceptionHandler(value = { EmailVerificationFailedException.class })
    ResponseEntity<EmailVerificationFailedExceptionBody> handleEmailVerificationFailedExceptionBody(Exception e) {
        EmailVerificationFailedExceptionBody exceptionBody = new EmailVerificationFailedExceptionBody(
                e.getMessage(), HttpStatus.UNAUTHORIZED, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionBody);
    }

}
