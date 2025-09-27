package com.gadget.rental.auth.verification;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class EmailVerificationCleaner {

    private static Logger LOGGER = LoggerFactory.getLogger(EmailVerificationCleaner.class);

    private final EmailVerificationRepository emailVerificationRepository;

    @Autowired
    public EmailVerificationCleaner(EmailVerificationRepository emailVerificationRepository) {
        this.emailVerificationRepository = emailVerificationRepository;
    }

    @Scheduled(fixedDelay = 3_600_000)
    public void removeExpiredCode() {
        ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneId.of("Z"));
        emailVerificationRepository.deleteExpiredEmailVerificationByExpiry(currentDateTime);
        LOGGER.info(String.format("Deleted expired verification codes at %s.", currentDateTime.toString()));
    }
}
