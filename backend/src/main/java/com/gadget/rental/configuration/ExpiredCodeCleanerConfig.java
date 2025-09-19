package com.gadget.rental.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.gadget.rental.email.EmailVerificationCleaner;
import com.gadget.rental.email.EmailVerificationRepository;

@Configuration
@EnableScheduling
public class ExpiredCodeCleanerConfig {

    @Bean
    EmailVerificationCleaner getEmailVerificationCleaner(EmailVerificationRepository emailVerificationRepository) {
        return new EmailVerificationCleaner(emailVerificationRepository);
    }

}
