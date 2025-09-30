package com.gadget.rental.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;

public class JwtUtility {
    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SIG.HS256.toString());

    public String generateJwtToken(String email) {
        Map<String, String> jwtHeaders = new HashMap<>();
        jwtHeaders.put("", "");

        return Jwts.builder()
                .header()
                .add(jwtHeaders).and()
                .signWith(key)
                .claim("email", email)
                .compact();
    }

    // TODO : Validate tokens

    // TODO :
}
