package com.gadget.rental.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("prod")
@PropertySource("classpath:application-prod.properties")
public class ProdDatabaseConfig {

    @Value("${prod.datasource.username}")
    private String username;

    @Value("${prod.datasource.password}")
    private String password;

    @Value("${prod.datasource.url}")
    private String url;

    @Value("${prod.datasource.driver-class-name}")
    private String driverClassName;

    @Bean
    DataSource getDataSource() {
        return DataSourceBuilder
                .create()
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .url(url)
                .build();
    }
}
