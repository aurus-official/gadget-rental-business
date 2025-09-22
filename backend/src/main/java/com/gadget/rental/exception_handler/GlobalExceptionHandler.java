package com.gadget.rental.exception_handler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import com.gadget.rental.exception.ClientExistedException;
import com.gadget.rental.exception.EmailAlreadyBoundException;
import com.gadget.rental.exception.EmailVerificationExpiredException;
import com.gadget.rental.exception.EmailVerificationFailedException;
import com.gadget.rental.exception.EmailVerificationInProgressException;
import com.gadget.rental.exception.EmailVerificationRequestNotExistedException;
import com.gadget.rental.exception.EmailVerificationResendTooSoonException;
import com.gadget.rental.exception.InvalidEmailVerificationCodeException;
import com.gadget.rental.exception_body.ClientExistedExceptionBody;
import com.gadget.rental.exception_body.EmailAlreadyBoundExceptionBody;
import com.gadget.rental.exception_body.EmailVerificationExpiredExceptionBody;
import com.gadget.rental.exception_body.EmailVerificationFailedExceptionBody;
import com.gadget.rental.exception_body.EmailVerificationInProgressExceptionBody;
import com.gadget.rental.exception_body.EmailVerificationRequestNotExistedExceptionBody;
import com.gadget.rental.exception_body.EmailVerificationResendTooSoonExceptionBody;
import com.gadget.rental.exception_body.InvalidEmailVerificationCodeExceptionBody;

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
    ResponseEntity<EmailVerificationFailedExceptionBody> handleEmailVerificationFailedException(Exception e) {
        EmailVerificationFailedExceptionBody exceptionBody = new EmailVerificationFailedExceptionBody(
                e.getMessage(), HttpStatus.UNAUTHORIZED, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionBody);
    }

    @ExceptionHandler(value = { EmailAlreadyBoundException.class })
    ResponseEntity<EmailAlreadyBoundExceptionBody> handleEmailAlreadyBoundException(Exception e) {
        EmailAlreadyBoundExceptionBody exceptionBody = new EmailAlreadyBoundExceptionBody(
                e.getMessage(), HttpStatus.UNAUTHORIZED, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionBody);
    }

    @ExceptionHandler(value = { EmailVerificationExpiredException.class })
    ResponseEntity<EmailVerificationExpiredExceptionBody> handleEmailVerificationExpiredException(
            Exception e) {
        EmailVerificationExpiredExceptionBody exceptionBody = new EmailVerificationExpiredExceptionBody(
                e.getMessage(), HttpStatus.UNAUTHORIZED, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionBody);
    }

    @ExceptionHandler(value = { EmailVerificationRequestNotExistedException.class })
    ResponseEntity<EmailVerificationRequestNotExistedExceptionBody> handleEmailVerificationRequestNotExistedException(
            Exception e) {
        EmailVerificationRequestNotExistedExceptionBody exceptionBody = new EmailVerificationRequestNotExistedExceptionBody(
                e.getMessage(), HttpStatus.UNAUTHORIZED, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionBody);
    }

    @ExceptionHandler(value = { EmailVerificationResendTooSoonException.class })
    ResponseEntity<EmailVerificationResendTooSoonExceptionBody> handleEmailVerificationResendTooSoonException(
            Exception e) {
        EmailVerificationResendTooSoonExceptionBody exceptionBody = new EmailVerificationResendTooSoonExceptionBody(
                e.getMessage(), HttpStatus.TOO_MANY_REQUESTS, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(exceptionBody);
    }

    @ExceptionHandler(value = { InvalidEmailVerificationCodeException.class })
    ResponseEntity<InvalidEmailVerificationCodeExceptionBody> handleInvalidEmailVerificationCodeException(
            Exception e) {
        InvalidEmailVerificationCodeExceptionBody exceptionBody = new InvalidEmailVerificationCodeExceptionBody(
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }

    @ExceptionHandler(value = { EmailVerificationInProgressException.class })
    ResponseEntity<EmailVerificationInProgressExceptionBody> handleInvalidEmailVerificationInProgressException(
            Exception e) {
        EmailVerificationInProgressExceptionBody exceptionBody = new EmailVerificationInProgressExceptionBody(
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }
}
