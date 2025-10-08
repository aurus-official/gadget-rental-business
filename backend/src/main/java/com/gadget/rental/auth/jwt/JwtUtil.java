package com.gadget.rental.auth.jwt;

import java.io.IOException;
import java.security.Key;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.spec.SecretKeySpec;

import com.gadget.rental.exception.JwtAuthenticationException;
import com.gadget.rental.exception.JwtExpiredAuthenticationException;
import com.gadget.rental.shared.AccountType;
import com.gadget.rental.shared.ErrorMessageBodyUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {

    private final String SIGNATURE_ALGORITHM = "HmacSHA256";
    private final JwtKeyManager jwtKeyManager;
    private final int JWT_ACCESS_TOKEN_VALIDITY_MINUTE = 15;
    private final int JWT_REFRESH_TOKEN_VALIDITY_MONTH = 1;
    private final JwtRefreshTokenRepository refreshTokenRepository;

    @Value("${admin.mail.address}")
    private String issuer;

    @Autowired
    public JwtUtil(JwtKeyManager jwtKeyManager, JwtRefreshTokenRepository refreshTokenRepository) {
        this.jwtKeyManager = jwtKeyManager;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String generateAccessJwtToken(String email, AccountType role) {
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
                .expiration(Date.from(
                        ZonedDateTime.now(ZoneId.of("Z")).plusMinutes(JWT_ACCESS_TOKEN_VALIDITY_MINUTE).toInstant()))
                .issuer(issuer)
                .claim("role", role.value)
                .compact();
    }

    public Jws<Claims> validateJwtToken(String token) throws JwtException {
        JwtParser parser = Jwts.parser().keyLocator((header -> {

            String keyId = header.get("kid").toString();

            if (keyId == null) {
                throw new JwtException("Jwt access token is invalid.");
            }

            String secretKey = jwtKeyManager.getAllActiveKeysMap().get(keyId);

            if (secretKey == null) {
                throw new JwtException("Jwt access token is invalid.");
            }

            Key key = new SecretKeySpec(JwtKeyBase64Util.decodeJwtSecretKey(secretKey),
                    SIGNATURE_ALGORITHM);
            return key;
        })).build();

        Jws<Claims> claims = parser.parseSignedClaims(token);
        return claims;
    }

    public String generateRefreshJwtToken(String email, AccountType role) {
        JwtKeyModel primaryKey = jwtKeyManager.getPrimaryJwtKey();
        Key key = new SecretKeySpec(
                JwtKeyBase64Util
                        .decodeJwtSecretKey(jwtKeyManager.getAllActiveKeysMap().get(primaryKey.getKeyId())),
                SIGNATURE_ALGORITHM);

        ZonedDateTime validFrom = ZonedDateTime.now(ZoneId.of("Z"));
        ZonedDateTime validUntil = ZonedDateTime.now(ZoneId.of("Z")).plusMonths(JWT_REFRESH_TOKEN_VALIDITY_MONTH);

        String refreshJwtToken = Jwts.builder()
                .header()
                .keyId(primaryKey.getKeyId())
                .and()
                .subject(email)
                .signWith(key)
                .issuedAt(Date.from(validFrom.toInstant()))
                .expiration(Date.from(validUntil.toInstant()))
                .issuer(issuer)
                .compact();

        refreshTokenRepository.findRefreshTokenByEmail(email).ifPresent((jwtRefreshToken) -> {
            refreshTokenRepository.delete(jwtRefreshToken);
        });

        JwtRefreshTokenModel refreshTokenModel = new JwtRefreshTokenModel();
        refreshTokenModel.setEmail(email);
        refreshTokenModel.setStatus(JwtRefreshTokenStatus.ACTIVE);
        refreshTokenModel.setToken(refreshJwtToken);
        refreshTokenModel.setValidFrom(validFrom);
        refreshTokenModel.setValidUntil(validUntil);
        refreshTokenModel.setAccountType(role);

        refreshTokenRepository.save(refreshTokenModel);

        return refreshJwtToken;
    }

    public JwtRefreshTokenModel validateRefreshJwtToken(String token) {
        JwtParser parser = Jwts.parser().keyLocator((header -> {
            String keyId = header.get("kid").toString();

            if (keyId == null) {
                throw new JwtAuthenticationException("Jwt refresh token is invalid.");
            }

            String secretKey = jwtKeyManager.getAllActiveKeysMap().get(keyId);

            if (secretKey == null) {
                throw new JwtAuthenticationException("Jwt refresh token is invalid.");
            }

            Key key = new SecretKeySpec(JwtKeyBase64Util.decodeJwtSecretKey(secretKey),
                    SIGNATURE_ALGORITHM);
            return key;
        })).build();

        try {
            Jws<Claims> claims = parser.parseSignedClaims(token);
            Claims payload = claims.getPayload();
            String subject = payload.getSubject();

            JwtRefreshTokenModel refreshTokenModel = refreshTokenRepository.findRefreshTokenByEmail(subject)
                    .orElseThrow(
                            () -> new JwtAuthenticationException("Refresh token does not exist or has been revoked."));

            if (!(refreshTokenModel.getToken().equals(token))) {
                throw new JwtAuthenticationException("Jwt refresh tokens didn't match.");
            }

            if (!(refreshTokenModel.getStatus().equals(JwtRefreshTokenStatus.ACTIVE))) {
                throw new JwtAuthenticationException("Jwt refresh tokens is not active.");
            }

            return refreshTokenModel;

        } catch (ExpiredJwtException e) {
            throw new JwtExpiredAuthenticationException("Jwt refresh token is expired.");

        } catch (IllegalArgumentException e) {
            throw new JwtAuthenticationException("Jwt refresh token is invalid.");

        } catch (JwtException e) {
            throw new JwtAuthenticationException("Jwt refresh token is invalid.");
        }
    }

    public String extractJwtToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String header = request.getHeader("Authorization");

        if (header == null) {
            String message = ErrorMessageBodyUtil.generateErrorMessageBody("Authorization header is missing.");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(message);
            return null;
        }

        if (!header.startsWith("Bearer")) {
            String message = ErrorMessageBodyUtil.generateErrorMessageBody("Bearer token is missing.");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(message);
            return null;
        }
        return header.substring(7);
    }

    public void blacklistJwtToken(String token) {
        JwtParser parser = Jwts.parser().keyLocator((header -> {
            String keyId = header.get("kid").toString();

            if (keyId == null) {
                throw new JwtAuthenticationException("Jwt refresh token is invalid.");
            }

            String secretKey = jwtKeyManager.getAllActiveKeysMap().get(keyId);

            if (secretKey == null) {
                throw new JwtAuthenticationException("Jwt refresh token is invalid.");
            }

            Key key = new SecretKeySpec(JwtKeyBase64Util.decodeJwtSecretKey(secretKey),
                    SIGNATURE_ALGORITHM);
            return key;
        })).build();

        try {
            Jws<Claims> claims = parser.parseSignedClaims(token);
            Claims payload = claims.getPayload();
            String subject = payload.getSubject();

            JwtRefreshTokenModel refreshTokenModel = refreshTokenRepository.findRefreshTokenByEmail(subject)
                    .orElseThrow(
                            () -> new JwtAuthenticationException("Refresh token does not exist or has been revoked."));

            if (!(refreshTokenModel.getToken().equals(token))) {
                throw new JwtAuthenticationException("Jwt refresh tokens didn't match.");
            }

            if (!(refreshTokenModel.getStatus().equals(JwtRefreshTokenStatus.ACTIVE))) {
                throw new JwtAuthenticationException("Jwt refresh tokens is not active.");
            }

            refreshTokenRepository.setRefreshTokenByEmail(refreshTokenModel.getEmail(), JwtRefreshTokenStatus.REVOKED);

        } catch (ExpiredJwtException e) {
            throw new JwtExpiredAuthenticationException("Jwt refresh token is expired.");

        } catch (IllegalArgumentException e) {
            throw new JwtAuthenticationException("Jwt refresh token is invalid.");

        } catch (JwtException e) {
            throw new JwtAuthenticationException("Jwt refresh token is invalid.");
        }

    }
}
