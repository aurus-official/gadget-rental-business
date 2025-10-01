package com.gadget.rental.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.gadget.rental.auth.verification.EmailVerificationCleaner;
import com.gadget.rental.auth.verification.EmailVerificationRepository;

@Profile("prod")
@Configuration
@EnableScheduling
public class ExpiredCodeCleanerConfig {

    @Bean
    EmailVerificationCleaner getEmailVerificationCleaner(EmailVerificationRepository emailVerificationRepository) {
        return new EmailVerificationCleaner(emailVerificationRepository);
    }
}
