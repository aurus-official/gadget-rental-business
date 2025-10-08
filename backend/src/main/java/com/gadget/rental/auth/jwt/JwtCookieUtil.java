package com.gadget.rental.auth.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.gadget.rental.exception.MissingRefreshTokenException;

public class JwtCookieUtil {
    private static final int JWT_REFRESH_TOKEN_VALIDITY_MONTHS = 1;

    public static void createHttpOnlyCookieForJwt(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(JWT_REFRESH_TOKEN_VALIDITY_MONTHS);
        response.addCookie(cookie);
    }

    public static String extractHttpOnlyCookieForJwt(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().compareTo("refresh_token") == 0) {
                    return cookie.getValue();
                }
            }
        }

        throw new MissingRefreshTokenException("Refresh token is missing");
    }
}
