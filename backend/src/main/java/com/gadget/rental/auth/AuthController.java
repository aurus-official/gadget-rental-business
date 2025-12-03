package com.gadget.rental.auth;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class AuthController {

    private final AuthService authService;

    @Autowired
    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "/auth/login")
    ResponseEntity<Map<String, String>> loginJwt(@Valid @RequestBody AuthDTO authDTO,
            HttpServletResponse response) {
        Map<String, String> tokens = authService.loginToGetJwtAccessTokenAndJwtRefreshToken(authDTO, response);

        return ResponseEntity.ok().body(tokens);
    }

    @PostMapping(path = "/auth/refresh")
    ResponseEntity<Map<String, String>> refreshTokens(@RequestHeader(name = "Authorization") String authHeader,
            HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> tokens = authService.refreshToGetJwtAccessTokenAndJwtRefreshToken(request, response);

        return ResponseEntity.ok().body(tokens);
    }

    @PostMapping(path = "/auth/logout")
    ResponseEntity<String> logoutJwt(@RequestHeader(name = "Authorization") String authHeader,
            HttpServletRequest request, HttpServletResponse response) {
        authService.logoutToBlackListAccessToken(request, response);
        return ResponseEntity.ok().body("Logged out successfully.");
    }
}
