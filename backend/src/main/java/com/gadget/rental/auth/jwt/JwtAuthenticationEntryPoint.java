package com.gadget.rental.auth.jwt;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.gadget.rental.exception.JwtAuthenticationException;
import com.gadget.rental.exception.JwtExpiredAuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        if (authException instanceof JwtExpiredAuthenticationException) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(authException.getMessage());
            return;
        }

        if (authException instanceof JwtAuthenticationException) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(authException.getMessage());
            return;
        }

    }

}
