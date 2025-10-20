package com.gadget.rental.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.gadget.rental.rental.RentalGadgetRepository;
import com.gadget.rental.rental.RentalGadgetStartupTask;

@Profile("prod")
@Configuration
@EnableScheduling
public class RentalGadgetStartupTaskConfig {

    @Bean
    public RentalGadgetStartupTask getRentalGadgetStartupTask(RentalGadgetRepository rentalGadgetRepository) {
        return new RentalGadgetStartupTask(rentalGadgetRepository);
    }
}
