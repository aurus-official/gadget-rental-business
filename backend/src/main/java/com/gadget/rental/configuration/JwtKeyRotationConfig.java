package com.gadget.rental.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.gadget.rental.auth.jwt.JwtKeyManager;
import com.gadget.rental.auth.jwt.JwtKeyRepository;

@Profile("prod")
@Configuration
@EnableScheduling
public class JwtKeyRotationConfig {

    @Bean
    JwtKeyManager getJwtKeyManager(JwtKeyRepository jwtKeyRepository) {
        return new JwtKeyManager(jwtKeyRepository);
    }
}
