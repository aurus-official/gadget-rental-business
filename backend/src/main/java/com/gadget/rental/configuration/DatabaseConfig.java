package com.gadget.rental.configuration;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

    @Bean
    DataSource getDataSource() {
        return DataSourceBuilder
                .create()
                .username("rentaluser")
                .password("rentalpass")
                .driverClassName("org.mariadb.jdbc.Driver")
                .url("jdbc:mariadb://mariadb:3306/rental-db")
                .build();
    }
}
