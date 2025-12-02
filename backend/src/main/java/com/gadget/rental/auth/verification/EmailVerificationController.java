package com.gadget.rental.auth.verification;

import jakarta.validation.Valid;

import com.gadget.rental.shared.AccountType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/v1")
@RestController
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    EmailVerificationController(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    @PostMapping(path = "/client/email-verification-requests")
    ResponseEntity<EmailVerificationResponseDTO> client_startEmailVerification(
            @Valid @RequestBody EmailDTO emailDTO) {
        EmailVerificationResponseDTO emailVerificationResponseDTO = emailVerificationService
                .createVerification(emailDTO, AccountType.CLIENT);
        return ResponseEntity.ok(emailVerificationResponseDTO);
    }

    @PostMapping(path = "/client/email-verification")
    ResponseEntity<String> client_verifyEmailVerification(
            @Valid @RequestBody EmailVerificationRequestDTO emailVerificationRequestDTO) {
        String token = emailVerificationService.verifyVerification(emailVerificationRequestDTO, AccountType.CLIENT);
        return ResponseEntity.ok(token);
    }

    @PostMapping(path = "/client/email-verification-requests/resend")
    ResponseEntity<EmailVerificationResponseDTO> client_resendEmailVerification(@Valid @RequestBody EmailDTO emailDTO) {
        EmailVerificationResponseDTO emailVerificationResponseDTO = emailVerificationService
                .resendVerification(emailDTO, AccountType.CLIENT);
        return ResponseEntity.ok(emailVerificationResponseDTO);
    }

    @PostMapping(path = "/admin/email-verification-requests")
    ResponseEntity<EmailVerificationResponseDTO> admin_startEmailVerification(
            @Valid @RequestBody EmailDTO emailDTO) {
        EmailVerificationResponseDTO emailVerificationResponseDTO = emailVerificationService
                .createVerification(emailDTO, AccountType.ADMIN);
        return ResponseEntity.ok(emailVerificationResponseDTO);
    }

    @PostMapping(path = "/admin/email-verification")
    ResponseEntity<String> admin_verifyEmailVerification(
            @Valid @RequestBody EmailVerificationRequestDTO emailVerificationRequestDTO) {
        String token = emailVerificationService.verifyVerification(emailVerificationRequestDTO, AccountType.ADMIN);
        return ResponseEntity.ok(String.format("Token : %s.", token));
    }

    @PostMapping(path = "/admin/email-verification-requests/resend")
    ResponseEntity<EmailVerificationResponseDTO> admin_resendEmailVerification(@Valid @RequestBody EmailDTO emailDTO) {
        EmailVerificationResponseDTO emailVerificationResponseDTO = emailVerificationService
                .resendVerification(emailDTO, AccountType.ADMIN);
        return ResponseEntity.ok(emailVerificationResponseDTO);
    }

}
