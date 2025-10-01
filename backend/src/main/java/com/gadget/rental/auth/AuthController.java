package com.gadget.rental.auth;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;

import com.gadget.rental.auth.jwt.JwtUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtility jwtUtility;

    @Autowired
    AuthController(AuthenticationManager authenticationManager, JwtUtility jwtUtility) {
        this.authenticationManager = authenticationManager;
        this.jwtUtility = jwtUtility;
    }

    @PostMapping(path = "/auth/login")
    ResponseEntity<String> loginAccount(@Valid @RequestBody AuthDTO authDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                authDTO.email(), authDTO.password());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (!(authentication.isAuthenticated())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
        }
        List<? extends GrantedAuthority> currentRoles = new ArrayList<>(authentication.getAuthorities());
        String role = currentRoles.get(0).toString();
        String token = jwtUtility.generateAccessJwtToken(authDTO.email(), AuthRoleType.valueOf(role.toUpperCase()));

        return ResponseEntity.ok(String.format("Access token : %s", token));
    }

    @PostMapping(path = "/testing")
    ResponseEntity<String> testingAccount(@RequestHeader(name = "Authorization") String authHeader) {
        return ResponseEntity.ok("DONE");
    }
}
