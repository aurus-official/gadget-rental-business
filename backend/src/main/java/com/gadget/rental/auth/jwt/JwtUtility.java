package com.gadget.rental.auth.jwt;

import java.security.Key;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import com.gadget.rental.auth.AuthRoleType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtility {

    private final String SIGNATURE_ALGORITHM = "HmacSHA256";
    private final JwtKeyManager jwtKeyManager;

    @Value("${admin.mail.address}")
    private String issuer;

    @Autowired
    public JwtUtility(JwtKeyManager jwtKeyManager) {
        this.jwtKeyManager = jwtKeyManager;
    }

    public String generateAccessJwtToken(String email, AuthRoleType role) {
        JwtKeyModel primaryKey = jwtKeyManager.getPrimaryJwtKey();
        Key key = new SecretKeySpec(
                JwtKeyBase64Util
                        .decodeJwtSecretKey(jwtKeyManager.getAllActiveKeysMap().get(primaryKey.getKeyId())),
                SIGNATURE_ALGORITHM);

        return Jwts.builder()
                .header()
                .keyId(primaryKey.getKeyId())
                .and()
                .subject(email)
                .signWith(key)
                .issuedAt(Date.from(ZonedDateTime.now(ZoneId.of("Z")).toInstant()))
                .expiration(Date.from(ZonedDateTime.now(ZoneId.of("Z")).plusMinutes(1).toInstant()))
                .issuer(issuer)
                .claim("role", role.value)
                .compact();
    }

    public Jws<Claims> validateJwtToken(String token) throws JwtException {
        JwtParser parser = Jwts.parser().keyLocator((header -> {
            System.out.println(header.get("kid"));

            String keyId = header.get("kid").toString();

            if (keyId == null) {
                throw new JwtException("Invalid Jwt token.");
            }

            String secretKey = jwtKeyManager.getAllActiveKeysMap().get(keyId);

            if (secretKey == null) {
                throw new JwtException("Invalid Jwt token.");
            }

            Key key = new SecretKeySpec(JwtKeyBase64Util.decodeJwtSecretKey(secretKey),
                    SIGNATURE_ALGORITHM);
            return key;
        })).build();

        Jws<Claims> claims = parser.parseSignedClaims(token);
        return claims;
    }

    public String generateRefreshJwtToken(String email, AuthRoleType role) {
        JwtKeyModel primaryKey = jwtKeyManager.getPrimaryJwtKey();
        Key key = new SecretKeySpec(
                JwtKeyBase64Util
                        .decodeJwtSecretKey(jwtKeyManager.getAllActiveKeysMap().get(primaryKey.getKeyId())),
                SIGNATURE_ALGORITHM);

        return Jwts.builder()
                .header()
                .keyId(primaryKey.getKeyId())
                .and()
                .subject(email)
                .signWith(key)
                .issuedAt(Date.from(ZonedDateTime.now(ZoneId.of("Z")).toInstant()))
                .expiration(Date.from(ZonedDateTime.now(ZoneId.of("Z")).plusMinutes(30).toInstant()))
                .issuer(issuer)
                .claim("role", role.value)
                .compact();
    }

    public Jws<Claims> validateRefreshJwtToken(String token) {
        JwtParser parser = Jwts.parser().keyLocator((header -> {
            System.out.println(header.get("kid"));
            Key key = new SecretKeySpec(
                    JwtKeyBase64Util
                            .decodeJwtSecretKey(jwtKeyManager.getAllActiveKeysMap().get(header.get("kid"))),
                    SIGNATURE_ALGORITHM);
            return key;
        })).build();
        Jws<Claims> claims = parser.parseSignedClaims(token);

        return claims;
    }
}
