package com.gadget.rental.email;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/v1/verification")
@RestController
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    EmailVerificationController(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    @PostMapping(path = "/email")
    ResponseEntity<String> startEmailVerification(
            @Valid @RequestBody EmailDTO emailDTO) {
        String verificationCode = emailVerificationService.createVerificationCodeModel(emailDTO);
        return ResponseEntity.ok(verificationCode);
    }

    @PostMapping(path = "/email/verify")
    ResponseEntity<String> verifyEmailVerification(@Valid @RequestBody EmailVerificationDTO emailVerificationDTO) {
        String token = emailVerificationService.verifyVerificationCodeModel(emailVerificationDTO);
        return ResponseEntity.ok(token);
    }

    @PostMapping(path = "/email/resend")
    ResponseEntity<String> resendEmailVerification(@Valid @RequestBody EmailDTO emailDTO) {
        emailVerificationService.resendVerificationCodeModel(emailDTO);
        return ResponseEntity.ok("DONE");
    }

    @GetMapping(path = "/email/test")
    ResponseEntity<String> testing() {
        return ResponseEntity.ok("TESTING");
    }
}
