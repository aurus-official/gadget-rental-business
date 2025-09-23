package com.gadget.rental.configuration;

import com.gadget.rental.email.EmailVerificationCleaner;
import com.gadget.rental.email.EmailVerificationRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Profile("prod")
@Configuration
@EnableScheduling
public class ExpiredCodeCleanerConfig {

    @Bean
    EmailVerificationCleaner getEmailVerificationCleaner(EmailVerificationRepository emailVerificationRepository) {
        return new EmailVerificationCleaner(emailVerificationRepository);
    }

}
