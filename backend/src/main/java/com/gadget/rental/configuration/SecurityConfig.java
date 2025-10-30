package com.gadget.rental.configuration;

import com.gadget.rental.auth.AuthUserDetailsService;
import com.gadget.rental.auth.jwt.JwtAuthenticationFilter;
import com.gadget.rental.auth.jwt.JwtUtil;
import com.gadget.rental.shared.AccountAccessDeniedHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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

    private final JwtUtil jwtUtil;

    @Autowired
    SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(
                        exceptionHandler -> exceptionHandler.accessDeniedHandler(getAccountAccessDeniedHandler()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/v1/clients").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/client/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/client/*/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/admins").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/admin/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/admin/*/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/auth/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/webhooks/payment").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/webhooks/test").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/gadgets").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/v1/bookings/admin").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/v1/bookings/client").hasAuthority("CLIENT")
                        .requestMatchers(HttpMethod.GET, "/v1/client/{username}").hasVariable("username")
                        .equalTo(Authentication::getName)
                        .anyRequest().authenticated())
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
    AuthenticationManager getAuthenticationManager(AuthUserDetailsService authUserDetailsService) throws Exception {
        DaoAuthenticationProvider userDaoAuthenticationProvider = new DaoAuthenticationProvider(
                authUserDetailsService);
        userDaoAuthenticationProvider.setPasswordEncoder(getBCryptPasswordEncoder());
        return new ProviderManager(userDaoAuthenticationProvider);
    }

    @Bean
    JwtAuthenticationFilter getJwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil);
    }

    @Bean
    AccountAccessDeniedHandler getAccountAccessDeniedHandler() {
        return new AccountAccessDeniedHandler();
    }

}
