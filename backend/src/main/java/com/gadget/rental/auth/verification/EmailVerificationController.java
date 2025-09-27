package com.gadget.rental.auth.verification;

import jakarta.validation.Valid;

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
    ResponseEntity<String> client_startEmailVerification(
            @Valid @RequestBody EmailDTO emailDTO) {
        String verificationCode = emailVerificationService.createVerification(emailDTO, EmailVerificationType.CLIENT);
        return ResponseEntity.ok(String.format("Verification code : %s.", verificationCode));
    }

    @PostMapping(path = "/client/email-verification")
    ResponseEntity<String> client_verifyEmailVerification(
            @Valid @RequestBody EmailVerificationDTO emailVerificationDTO) {
        String token = emailVerificationService.verifyVerification(emailVerificationDTO, EmailVerificationType.CLIENT);
        return ResponseEntity.ok(String.format("Token : %s.", token));
    }

    @PostMapping(path = "/client/email-verification-requests/resend")
    ResponseEntity<String> client_resendEmailVerification(@Valid @RequestBody EmailDTO emailDTO) {
        String verificationCode = emailVerificationService.resendVerification(emailDTO, EmailVerificationType.CLIENT);
        return ResponseEntity.ok(String.format("Verification code : %s.", verificationCode));
    }

    @PostMapping(path = "/admin/email-verification-requests")
    ResponseEntity<String> admin_startEmailVerification(
            @Valid @RequestBody EmailDTO emailDTO) {
        String verificationCode = emailVerificationService.createVerification(emailDTO, EmailVerificationType.ADMIN);
        return ResponseEntity.ok(String.format("Verification code : %s.", verificationCode));
    }

    @PostMapping(path = "/admin/email-verification")
    ResponseEntity<String> admin_verifyEmailVerification(
            @Valid @RequestBody EmailVerificationDTO emailVerificationDTO) {
        String token = emailVerificationService.verifyVerification(emailVerificationDTO, EmailVerificationType.ADMIN);
        return ResponseEntity.ok(String.format("Token : %s.", token));
    }

    @PostMapping(path = "/admin/email-verification-requests/resend")
    ResponseEntity<String> admin_resendEmailVerification(@Valid @RequestBody EmailDTO emailDTO) {
        String verificationCode = emailVerificationService.resendVerification(emailDTO, EmailVerificationType.ADMIN);
        return ResponseEntity.ok(String.format("Verification code : %s.", verificationCode));
    }

}
