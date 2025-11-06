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
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
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
                        // .requestMatchers(HttpMethod.GET,
                        // "/v1/bookings/users/{email}").hasVariable("email")
                        // .equalTo(Authentication::getName)

                        .requestMatchers(HttpMethod.POST, "/v1/clients").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/client/email-verification").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/client/email-verification-requests").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/client/email-verification-requests/resend").permitAll()

                        .requestMatchers(HttpMethod.POST, "/v1/admins").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/admin/email-verification").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/admin/email-verification-requests").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/admin/email-verification-requests/resend").permitAll()

                        .requestMatchers(HttpMethod.POST, "/v1/admin/bookings").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/v1/admin/bookings/client/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/v1/admin/bookings/*/client/*").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/v1/auth/*").permitAll()

                        .requestMatchers(HttpMethod.POST, "/v1/gadgets").hasAuthority("ADMIN")
                        .requestMatchers("/v1/gadgets/*").hasAuthority("ADMIN")
                        .requestMatchers("/v1/gadgets/images/*").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/v1/cash-payments").hasAuthority("ADMIN")
                        .requestMatchers("/v1/cash-payments/*/checkouts/*").hasAuthority("ADMIN")
                        .requestMatchers("/v1/cash-deposit/*").hasAuthority("ADMIN")

                        // .requestMatchers(HttpMethod.POST,
                        // "/v1/online-payments").hasAuthority("ADMIN")
                        // .requestMatchers(HttpMethod.POST,
                        // "/v1/online-payments/*").hasAuthority("ADMIN")
                        .requestMatchers("/v1/online-preauth/*").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/v1/webhooks/cash-payment").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/v1/webhooks/payment").permitAll()

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
