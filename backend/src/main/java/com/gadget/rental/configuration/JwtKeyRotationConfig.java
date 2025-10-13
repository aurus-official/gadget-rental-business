package com.gadget.rental.configuration;

import com.gadget.rental.auth.jwt.JwtKeyManager;
import com.gadget.rental.auth.jwt.JwtKeyRepository;
import com.gadget.rental.rental.RentalGadgetRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Profile("prod")
@Configuration
@EnableScheduling
public class JwtKeyRotationConfig {

    @Bean
    JwtKeyManager getJwtKeyManager(JwtKeyRepository jwtKeyRepository, RentalGadgetRepository rentalGadgetRepository) {
        return new JwtKeyManager(jwtKeyRepository, rentalGadgetRepository);
    }
}
