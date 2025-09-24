package com.gadget.rental.email;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class EmailVerificationCleaner {

    private final EmailVerificationRepository emailVerificationRepository;

    @Autowired
    public EmailVerificationCleaner(EmailVerificationRepository emailVerificationRepository) {
        this.emailVerificationRepository = emailVerificationRepository;
    }

    @Scheduled(fixedDelay = 3_600_000)
    public void removeExpiredCode() {
        ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneId.of("Z"));
        emailVerificationRepository.deleteExpiredEmailVerification(currentDateTime);
        System.out.println(String.format("Deleted expired verification codes at %s.", currentDateTime.toString()));
    }
}
