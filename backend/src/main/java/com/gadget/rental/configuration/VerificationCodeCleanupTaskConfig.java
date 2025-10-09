package com.gadget.rental.configuration;

import com.gadget.rental.auth.verification.EmailVerificationCleanupTask;
import com.gadget.rental.auth.verification.EmailVerificationRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Profile("prod")
@Configuration
@EnableScheduling
public class VerificationCodeCleanupTaskConfig {

    @Bean
    EmailVerificationCleanupTask getEmailVerificationCleanupTask(
            EmailVerificationRepository emailVerificationRepository) {
        return new EmailVerificationCleanupTask(emailVerificationRepository);
    }
}
