package com.gadget.rental.configuration;

import com.gadget.rental.auth.AuthUserDetailsService;
import com.gadget.rental.auth.jwt.JwtAuthenticationFilter;
import com.gadget.rental.auth.jwt.JwtUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthUserDetailsService authUserDetailsService;
    private final JwtUtility jwtUtility;

    @Autowired
    SecurityConfig(AuthUserDetailsService authUserDetailsService, JwtUtility jwtUtility) {
        this.authUserDetailsService = authUserDetailsService;
        this.jwtUtility = jwtUtility;
    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/v1/clients").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/client/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/client/*/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/admins").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/admin/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/admin/*/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/auth/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/client/{username}").hasVariable("username")
                        .equalTo(Authentication::getName)
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .authenticationProvider(getDaoAuthenticationProvider())
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(getJwtAuthenticationFilter(), AuthorizationFilter.class)
                .build();
    }

    @Bean
    BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    AuthenticationProvider getDaoAuthenticationProvider() {
        DaoAuthenticationProvider clientDaoAuthenticationProvider = new DaoAuthenticationProvider(
                authUserDetailsService);
        clientDaoAuthenticationProvider.setPasswordEncoder(getBCryptPasswordEncoder());
        return clientDaoAuthenticationProvider;
    }

    @Bean
    AuthenticationManager getAuthenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    JwtAuthenticationFilter getJwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtility);
    }
}
