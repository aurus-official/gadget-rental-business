package com.gadget.rental.configuration;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class TestDatabaseConfig {
    @Bean
    DataSource getDataSource() {
        return DataSourceBuilder
                .create()
                .build();
    }

}
