package com.gadget.rental.auth.verification;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class EmailVerificationCleanupTask {

    private static Logger LOGGER = LoggerFactory.getLogger(EmailVerificationCleanupTask.class);
    private final int EXPIRED_CODE_INTERVAL_MS = 86_400_000;

    private final EmailVerificationRepository emailVerificationRepository;

    @Autowired
    public EmailVerificationCleanupTask(EmailVerificationRepository emailVerificationRepository) {
        this.emailVerificationRepository = emailVerificationRepository;
    }

    @Scheduled(fixedDelay = EXPIRED_CODE_INTERVAL_MS, initialDelay = EXPIRED_CODE_INTERVAL_MS)
    public void removeExpiredCode() {
        ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneId.of("Z"));
        emailVerificationRepository.deleteExpiredEmailVerificationByExpiry(currentDateTime);
        LOGGER.info(String.format("Deleted expired verification codes at %s.", currentDateTime.toString()));
    }
}
