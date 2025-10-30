package com.gadget.rental.exception_handler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import com.gadget.rental.exception.AccountCreationTokenMismatchException;
import com.gadget.rental.exception.AdminAccountLimitExceededException;
import com.gadget.rental.exception.BookingConflictException;
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
import com.gadget.rental.exception.InvalidExcelFileException;
import com.gadget.rental.exception.InvalidImageFormatException;
import com.gadget.rental.exception.JwtAuthenticationException;
import com.gadget.rental.exception.JwtExpiredAuthenticationException;
import com.gadget.rental.exception.MissingRefreshTokenException;
import com.gadget.rental.exception.RentalGadgetAlreadyLeasedException;
import com.gadget.rental.exception.RentalGadgetExistedException;
import com.gadget.rental.exception.RentalGadgetImageExistedException;
import com.gadget.rental.exception.RentalGadgetMissingException;
import com.gadget.rental.exception.UsernameDuplicateException;
import com.gadget.rental.exception.UsernameNotFoundException;
import com.gadget.rental.exception_body.AccessDeniedExceptionBody;
import com.gadget.rental.exception_body.AccountCreationTokenMismatchExceptionBody;
import com.gadget.rental.exception_body.AdminAccountLimitExceededExceptionBody;
import com.gadget.rental.exception_body.BookingConflictExceptionBody;
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
import com.gadget.rental.exception_body.IllegalArgumentExceptionBody;
import com.gadget.rental.exception_body.InvalidEmailVerificationCodeExceptionBody;
import com.gadget.rental.exception_body.InvalidExcelFileExceptionBody;
import com.gadget.rental.exception_body.InvalidImageFormatExceptionBody;
import com.gadget.rental.exception_body.JwtAuthenticationExceptionBody;
import com.gadget.rental.exception_body.JwtExpiredAuthenticationExceptionBody;
import com.gadget.rental.exception_body.MissingRefreshTokenExceptionBody;
import com.gadget.rental.exception_body.MissingRequestHeaderExceptionBody;
import com.gadget.rental.exception_body.RentalGadgetAlreadyLeasedExceptionBody;
import com.gadget.rental.exception_body.RentalGadgetExistedExceptionBody;
import com.gadget.rental.exception_body.RentalGadgetImageExistedExceptionBody;
import com.gadget.rental.exception_body.RentalGadgetMissingExceptionBody;
import com.gadget.rental.exception_body.UsernameDuplicateExceptionBody;
import com.gadget.rental.exception_body.UsernameNotFoundExceptionBody;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
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

    @ExceptionHandler(value = { AccountCreationTokenMismatchException.class })
    ResponseEntity<AccountCreationTokenMismatchExceptionBody> handleTokenMismatchException(
            Exception e) {
        AccountCreationTokenMismatchExceptionBody exceptionBody = new AccountCreationTokenMismatchExceptionBody(
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

    @ExceptionHandler(value = { AdminAccountLimitExceededException.class })
    ResponseEntity<AdminAccountLimitExceededExceptionBody> handleAdminAccountLimitExceededException(
            Exception e) {
        AdminAccountLimitExceededExceptionBody exceptionBody = new AdminAccountLimitExceededExceptionBody(
                e.getMessage(), HttpStatus.FORBIDDEN, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionBody);
    }

    @ExceptionHandler(value = { MissingRequestHeaderException.class })
    ResponseEntity<MissingRequestHeaderExceptionBody> handleMissingRequestException(
            Exception e) {
        MissingRequestHeaderExceptionBody exceptionBody = new MissingRequestHeaderExceptionBody(
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }

    @ExceptionHandler(value = { UsernameNotFoundException.class })
    ResponseEntity<UsernameNotFoundExceptionBody> handleUsernameNotFoundException(
            Exception e) {
        UsernameNotFoundExceptionBody exceptionBody = new UsernameNotFoundExceptionBody(
                e.getMessage(), HttpStatus.UNAUTHORIZED, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionBody);
    }

    @ExceptionHandler(value = { JwtAuthenticationException.class })
    ResponseEntity<JwtAuthenticationExceptionBody> handleJwtAuthentcationException(
            Exception e) {
        JwtAuthenticationExceptionBody exceptionBody = new JwtAuthenticationExceptionBody(
                e.getMessage(), HttpStatus.UNAUTHORIZED, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionBody);
    }

    @ExceptionHandler(value = { JwtExpiredAuthenticationException.class })
    ResponseEntity<JwtExpiredAuthenticationExceptionBody> handleJwtExpiredAuthenticationExceptionBody(
            Exception e) {
        JwtExpiredAuthenticationExceptionBody exceptionBody = new JwtExpiredAuthenticationExceptionBody(
                e.getMessage(), HttpStatus.UNAUTHORIZED, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionBody);
    }

    @ExceptionHandler(value = { MissingRefreshTokenException.class })
    ResponseEntity<MissingRefreshTokenExceptionBody> handleMissingRefreshTokenException(
            Exception e) {
        MissingRefreshTokenExceptionBody exceptionBody = new MissingRefreshTokenExceptionBody(
                e.getMessage(), HttpStatus.UNAUTHORIZED, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionBody);
    }

    @ExceptionHandler(value = { AccessDeniedException.class })
    ResponseEntity<AccessDeniedExceptionBody> handleAccessDeniedException(
            Exception e) {
        AccessDeniedExceptionBody exceptionBody = new AccessDeniedExceptionBody(
                e.getMessage(), HttpStatus.UNAUTHORIZED, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionBody);
    }

    @ExceptionHandler(value = { RentalGadgetExistedException.class })
    ResponseEntity<RentalGadgetExistedExceptionBody> handleRentalGadgetExistedException(
            Exception e) {
        RentalGadgetExistedExceptionBody exceptionBody = new RentalGadgetExistedExceptionBody(
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }

    @ExceptionHandler(value = { RentalGadgetMissingException.class })
    ResponseEntity<RentalGadgetMissingExceptionBody> handleRentalGadgetMissingException(
            Exception e) {
        RentalGadgetMissingExceptionBody exceptionBody = new RentalGadgetMissingExceptionBody(
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }

    @ExceptionHandler(value = { InvalidExcelFileException.class })
    ResponseEntity<InvalidExcelFileExceptionBody> handleInvalidExcelFileException(
            Exception e) {
        InvalidExcelFileExceptionBody exceptionBody = new InvalidExcelFileExceptionBody(
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }

    @ExceptionHandler(value = { RentalGadgetImageExistedException.class })
    ResponseEntity<RentalGadgetImageExistedExceptionBody> handleRentalGadgetImageExistedException(
            Exception e) {
        RentalGadgetImageExistedExceptionBody exceptionBody = new RentalGadgetImageExistedExceptionBody(
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }

    @ExceptionHandler(value = { InvalidImageFormatException.class })
    ResponseEntity<InvalidImageFormatExceptionBody> handleInvalidImageFormatException(
            Exception e) {
        InvalidImageFormatExceptionBody exceptionBody = new InvalidImageFormatExceptionBody(
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }

    @ExceptionHandler(value = { RentalGadgetAlreadyLeasedException.class })
    ResponseEntity<RentalGadgetAlreadyLeasedExceptionBody> handleRentalGadgetAlreadyLeasedException(
            Exception e) {
        RentalGadgetAlreadyLeasedExceptionBody exceptionBody = new RentalGadgetAlreadyLeasedExceptionBody(
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    ResponseEntity<IllegalArgumentExceptionBody> handleIllegalArgumentException(
            Exception e) {
        IllegalArgumentExceptionBody exceptionBody = new IllegalArgumentExceptionBody(
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }

    @ExceptionHandler(value = { BookingConflictException.class })
    ResponseEntity<BookingConflictExceptionBody> handleBookingConflictException(
            Exception e) {
        BookingConflictExceptionBody exceptionBody = new BookingConflictExceptionBody(
                e.getMessage(), HttpStatus.CONFLICT, ZonedDateTime.now(ZoneId.of("Z")));

        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionBody);
    }
}
