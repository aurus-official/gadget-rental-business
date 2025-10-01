package com.gadget.rental.auth.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtility jwtUtility;

    @Autowired
    public JwtAuthenticationFilter(JwtUtility jwtUtility) {
        this.jwtUtility = jwtUtility;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        // TODO: Add refresh token validation.

        if (header != null) {
            if (!(header.startsWith("Bearer"))) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("JWT token is missing.");
                return;
            }
            String jwtToken = header.substring(7);

            if (jwtToken.length() < 37) {
                filterChain.doFilter(request, response);
                return;
            }

            Jws<Claims> claims = jwtUtility.validateJwtToken(jwtToken);
            Claims payload = claims.getPayload();

            if (payload.getSubject() == null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("JWT subject claim is missing.");
                return;
            }

            if (payload.get("role") == null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("JWT subject claim is missing.");
                return;
            }

            JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(claims);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        }

        filterChain.doFilter(request, response);
    }
}
