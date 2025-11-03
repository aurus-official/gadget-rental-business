package com.gadget.rental.configuration;

import com.gadget.rental.payment.PaymentResponseErrorHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new PaymentResponseErrorHandler());
        return restTemplate;
    }
}
