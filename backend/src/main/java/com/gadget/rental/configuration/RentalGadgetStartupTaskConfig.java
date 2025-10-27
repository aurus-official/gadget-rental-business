package com.gadget.rental.configuration;

import com.gadget.rental.rental.RentalGadgetRepository;
import com.gadget.rental.rental.RentalGadgetStartupTask;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Profile("prod")
@Configuration
@EnableScheduling
public class RentalGadgetStartupTaskConfig {

    @Bean
    RentalGadgetStartupTask getRentalGadgetStartupTask(RentalGadgetRepository rentalGadgetRepository) {
        return new RentalGadgetStartupTask(rentalGadgetRepository);
    }
}
