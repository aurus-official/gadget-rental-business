package com.gadget.rental.exception_handler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import com.gadget.rental.exception.ClientAccountExistedException;
import com.gadget.rental.exception.EmailAlreadyBoundException;
import com.gadget.rental.exception.EmailAlreadyVerifiedException;
import com.gadget.rental.exception.EmailNotVerifiedException;
import com.gadget.rental.exception.EmailVerificationAttemptLimitReachedException;
import com.gadget.rental.exception.EmailVerificationExpiredException;
import com.gadget.rental.exception.EmailVerificationFailedException;
import com.gadget.rental.exception.EmailVerificationInProgressException;
import com.gadget.rental.exception.EmailVerificationRequestNotExistedException;
import com.gadget.rental.exception.EmailVerificationResendTooSoonException;
import com.gadget.rental.exception.EmailVerificationRoleMismatchException;
import com.gadget.rental.exception.InvalidEmailVerificationCodeException;
import com.gadget.rental.exception.TokenMismatchException;
import com.gadget.rental.exception.UsernameDuplicateException;
import com.gadget.rental.exception_body.ClientAccountExistedExceptionBody;
import com.gadget.rental.exception_body.EmailAlreadyBoundExceptionBody;
import com.gadget.rental.exception_body.EmailAlreadyVerifiedExceptionBody;
import com.gadget.rental.exception_body.EmailNotVerifiedExceptionBody;
import com.gadget.rental.exception_body.EmailVerificationAttemptLimitReachedExceptionBody;
import com.gadget.rental.exception_body.EmailVerificationExpiredExceptionBody;
import com.gadget.rental.exception_body.EmailVerificationFailedExceptionBody;
import com.gadget.rental.exception_body.EmailVerificationInProgressExceptionBody;
import com.gadget.rental.exception_body.EmailVerificationRequestNotExistedExceptionBody;
import com.gadget.rental.exception_body.EmailVerificationResendTooSoonExceptionBody;
import com.gadget.rental.exception_body.EmailVerificationRoleMismatchExceptionBody;
import com.gadget.rental.exception_body.HttpMessageNotReadableExceptionBody;
import com.gadget.rental.exception_body.InvalidEmailVerificationCodeExceptionBody;
import com.gadget.rental.exception_body.TokenMismatchExceptionBody;
import com.gadget.rental.exception_body.UsernameDuplicateExceptionBody;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { ClientAccountExistedException.class })
    public ResponseEntity<ClientAccountExistedExceptionBody> handleClientAccountExistedException(Exception e) {
        ClientAccountExistedExceptionBody exceptionBody = new ClientAccountExistedExceptionBody(
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
                        (error) -> error.getDefaultMessage(), (existing, _) -> existing));
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
                e.getMessage(), HttpStatus.CONFLICT, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionBody);
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
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
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

    @ExceptionHandler(value = { EmailNotVerifiedException.class })
    ResponseEntity<EmailNotVerifiedExceptionBody> handleEmailNotVerifiedException(
            Exception e) {
        EmailNotVerifiedExceptionBody exceptionBody = new EmailNotVerifiedExceptionBody(
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }

    @ExceptionHandler(value = { TokenMismatchException.class })
    ResponseEntity<TokenMismatchExceptionBody> handleTokenMismatchException(
            Exception e) {
        TokenMismatchExceptionBody exceptionBody = new TokenMismatchExceptionBody(
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }

    @ExceptionHandler(value = { EmailAlreadyVerifiedException.class })
    ResponseEntity<EmailAlreadyVerifiedExceptionBody> handleEmailAlreadyVerified(
            Exception e) {
        EmailAlreadyVerifiedExceptionBody exceptionBody = new EmailAlreadyVerifiedExceptionBody(
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }

    @ExceptionHandler(value = { HttpMessageNotReadableException.class })
    ResponseEntity<HttpMessageNotReadableExceptionBody> handleHttpMessageNotReadableException(Exception e) {
        HttpMessageNotReadableExceptionBody exceptionBody = new HttpMessageNotReadableExceptionBody(
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }

    @ExceptionHandler(value = { UsernameDuplicateException.class })
    ResponseEntity<UsernameDuplicateExceptionBody> handleUsernameDuplicateException(Exception e) {
        UsernameDuplicateExceptionBody exceptionBody = new UsernameDuplicateExceptionBody(
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }

    @ExceptionHandler(value = { EmailVerificationAttemptLimitReachedException.class })
    ResponseEntity<EmailVerificationAttemptLimitReachedExceptionBody> handleEmailVerificationAttemptLimitException(
            Exception e) {
        EmailVerificationAttemptLimitReachedExceptionBody exceptionBody = new EmailVerificationAttemptLimitReachedExceptionBody(
                e.getMessage(), HttpStatus.TOO_MANY_REQUESTS, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(exceptionBody);
    }

    @ExceptionHandler(value = { EmailVerificationRoleMismatchException.class })
    ResponseEntity<EmailVerificationRoleMismatchExceptionBody> handleEmailVerificationRoleMismatchException(
            Exception e) {
        EmailVerificationRoleMismatchExceptionBody exceptionBody = new EmailVerificationRoleMismatchExceptionBody(
                e.getMessage(), HttpStatus.UNAUTHORIZED, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionBody);
    }
}
