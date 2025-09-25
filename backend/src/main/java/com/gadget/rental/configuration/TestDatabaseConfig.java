package com.gadget.rental.configuration;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Profile("test")
@Configuration
@PropertySource("classpath:application-test.properties")
public class TestDatabaseConfig {
    @Bean
    DataSource getDataSource() {
        return DataSourceBuilder
                .create()
                .username("sa")
                .password("password")
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:testdb")
                .build();
    }

}
