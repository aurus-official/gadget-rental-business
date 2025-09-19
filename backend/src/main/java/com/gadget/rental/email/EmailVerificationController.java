package com.gadget.rental.email;

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

    @PostMapping(path = "/emails")
    ResponseEntity<String> startEmailVerification(
            @Valid @RequestBody EmailDTO emailDTO) {
        String verificationCode = emailVerificationService.createVerificationCodeModel(emailDTO);
        return ResponseEntity.ok(verificationCode);
    }

    @PostMapping(path = "/emails/verify")
    ResponseEntity<String> verifyEmailVerification(@Valid @RequestBody EmailVerificationDTO emailVerificationDTO) {
        emailVerificationService.verifyVerificationCodeModel(emailVerificationDTO);
        return ResponseEntity.ok("VERIFIED");
    }
}
