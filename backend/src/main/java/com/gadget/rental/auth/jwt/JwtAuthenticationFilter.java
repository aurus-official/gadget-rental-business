package com.gadget.rental.auth.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.gadget.rental.exception.JwtAuthenticationException;
import com.gadget.rental.exception.JwtExpiredAuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtility jwtUtility;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    public JwtAuthenticationFilter(JwtUtility jwtUtility, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtUtility = jwtUtility;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    // FIX : ERROR IN ACCESS / CAN ACCESS WITHOUT CREDS

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null) {
            if (!(header.startsWith("Bearer"))) {
                filterChain.doFilter(request, response);
                return;
            }
            String jwtToken = header.substring(7);

            if (jwtToken.length() < 37) {
                filterChain.doFilter(request, response);
                return;
            }

            Claims payload = null;
            Jws<Claims> claims = null;

            try {
                claims = jwtUtility.validateJwtToken(jwtToken);
                payload = claims.getPayload();
            } catch (ExpiredJwtException _) {
                jwtAuthenticationEntryPoint.commence(request, response,
                        new JwtExpiredAuthenticationException("Expired Jwt token."));
                return;
            } catch (JwtException _) {
                jwtAuthenticationEntryPoint.commence(request, response,
                        new JwtAuthenticationException("Invalid Jwt token."));
                return;
            }

            if (payload == null || claims == null) {
                jwtAuthenticationEntryPoint.commence(request, response,
                        new JwtAuthenticationException("Invalid Jwt token."));
                return;
            }

            if (payload.getSubject() == null) {
                jwtAuthenticationEntryPoint.commence(request, response,
                        new JwtAuthenticationException("JWT subject claim is missing."));
                return;
            }

            if (payload.get("role") == null) {
                jwtAuthenticationEntryPoint.commence(request, response,
                        new JwtAuthenticationException("JWT role claim is missing."));
                return;
            }

            JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(claims);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        }

        filterChain.doFilter(request, response);
    }
}
