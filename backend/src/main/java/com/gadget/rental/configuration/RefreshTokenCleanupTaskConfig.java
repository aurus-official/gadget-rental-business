package com.gadget.rental.configuration;

import com.gadget.rental.auth.jwt.JwtRefreshTokenCleanupTask;
import com.gadget.rental.auth.jwt.JwtRefreshTokenRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Profile("prod")
@Configuration
@EnableScheduling
public class RefreshTokenCleanupTaskConfig {

    @Bean
    JwtRefreshTokenCleanupTask getJwtRefreshTokenCleanupTask(JwtRefreshTokenRepository refreshTokenRepository) {
        return new JwtRefreshTokenCleanupTask(refreshTokenRepository);
    }

}
