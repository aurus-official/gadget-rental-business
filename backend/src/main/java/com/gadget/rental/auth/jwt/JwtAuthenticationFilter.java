package com.gadget.rental.auth.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.gadget.rental.shared.ErrorMessageBodyUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final int UUID_LENGTH = 37;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();

        boolean matchesClients = requestURI.startsWith("/v1/client/") || requestURI.equals("/v1/clients");
        boolean matchesAdmin = requestURI.startsWith("/v1/admin/") || requestURI.equals("/v1/admins");
        boolean matchesAuth = requestURI.startsWith("/v1/auth/login");

        return matchesClients || matchesAdmin || matchesAuth;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwtToken = jwtUtil.extractJwtToken(request, response);

        if (request.getRequestURI().startsWith("/v1/auth/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (jwtToken != null) {
            if (jwtToken.length() < UUID_LENGTH) {
                filterChain.doFilter(request, response);
                return;
            }

            Jws<Claims> claims = null;

            try {
                claims = jwtUtil.validateJwtToken(jwtToken);
                Claims payload = claims.getPayload();

                String _ = payload.getSubject();
                Object _ = payload.get("role");

            } catch (ExpiredJwtException e) {
                String message = ErrorMessageBodyUtil.generateErrorMessageBody("Jwt token is expired.");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(message);
                return;

            } catch (UnsupportedJwtException e) {
                String message = ErrorMessageBodyUtil.generateErrorMessageBody("Jwt token is unsupported.");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(message);
                return;

            } catch (MalformedJwtException e) {
                String message = ErrorMessageBodyUtil.generateErrorMessageBody("Jwt token is malformed.");
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getWriter().write(message);
                return;

            } catch (SignatureException e) {
                String message = ErrorMessageBodyUtil.generateErrorMessageBody("Jwt signature is invalid.");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(message);
                return;

            } catch (IllegalArgumentException e) {
                String message = ErrorMessageBodyUtil.generateErrorMessageBody("Jwt token has missing claims.");
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getWriter().write(message);
                return;

            } catch (JwtException e) {
                String message = ErrorMessageBodyUtil.generateErrorMessageBody("Jwt token is invalid.");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(message);
                return;
            }

            JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(claims);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
            return;
        }
    }
}
