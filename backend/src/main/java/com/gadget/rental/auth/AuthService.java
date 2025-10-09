package com.gadget.rental.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.gadget.rental.auth.jwt.JwtCookieUtil;
import com.gadget.rental.auth.jwt.JwtRefreshTokenModel;
import com.gadget.rental.auth.jwt.JwtUtil;
import com.gadget.rental.exception.JwtAuthenticationException;
import com.gadget.rental.shared.AccountType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public Map<String, String> loginToGetJwtAccessTokenAndJwtRefreshToken(AuthDTO authDTO,
            HttpServletResponse response) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken
                .unauthenticated(authDTO.email(), authDTO.password());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        Map<String, String> tokens = new HashMap<>();

        List<? extends GrantedAuthority> currentRoles = new ArrayList<>(authentication.getAuthorities());
        String role = currentRoles.get(0).toString();

        String accessToken = jwtUtil.generateAccessJwtToken(authDTO.email(),
                AccountType.valueOf(role.toUpperCase()));

        String refreshToken = jwtUtil.generateRefreshJwtToken(authDTO.email(),
                AccountType.valueOf(role.toUpperCase()));

        JwtCookieUtil.createHttpOnlyCookieForJwt(response, refreshToken);

        tokens.put("access_token", accessToken);
        return tokens;
    }

    public Map<String, String> refreshToGetJwtAccessTokenAndJwtRefreshToken(HttpServletRequest request,
            HttpServletResponse response) {

        try {
            String jwtToken = jwtUtil.extractJwtToken(request, response);
            JwtRefreshTokenModel refreshTokenModel = jwtUtil.validateRefreshJwtToken(jwtToken);

            String accessToken = jwtUtil.generateAccessJwtToken(refreshTokenModel.getEmail(),
                    refreshTokenModel.getAccountType());

            String refreshToken = jwtUtil.generateRefreshJwtToken(refreshTokenModel.getEmail(),
                    refreshTokenModel.getAccountType());

            JwtCookieUtil.createHttpOnlyCookieForJwt(response, refreshToken);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", accessToken);

            return tokens;
        } catch (IOException e) {
            throw new JwtAuthenticationException("Jwt token is invalid.");
        }
    }

    public void logoutToBlackListAccessToken(HttpServletRequest request,
            HttpServletResponse response) {
        String jwtToken = JwtCookieUtil.extractHttpOnlyCookieForJwt(request);
        jwtUtil.blacklistJwtToken(jwtToken);

    }
}
