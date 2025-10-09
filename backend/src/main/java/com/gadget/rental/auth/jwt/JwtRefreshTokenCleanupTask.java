package com.gadget.rental.auth.jwt;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class JwtRefreshTokenCleanupTask {

    private static Logger LOGGER = LoggerFactory.getLogger(JwtRefreshTokenCleanupTask.class);
    private final int INVALID_REFRESH_TOKEN_INTERVAL_MS = 14_400_000;
    private final JwtRefreshTokenRepository refreshTokenRepository;

    public JwtRefreshTokenCleanupTask(JwtRefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Scheduled(initialDelay = INVALID_REFRESH_TOKEN_INTERVAL_MS, fixedDelay = INVALID_REFRESH_TOKEN_INTERVAL_MS)
    public void removeInvalidRefreshToken() {
        ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneId.of("Z"));
        refreshTokenRepository.deleteInvalidRefreshTokenByExpiry(currentDateTime);
        LOGGER.info(String.format("Deleted invalid refresh tokens at %s.", currentDateTime.toString()));
    }
}
