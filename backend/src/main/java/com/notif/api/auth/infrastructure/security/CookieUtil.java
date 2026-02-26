package com.notif.api.auth.infrastructure.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

import java.util.Arrays;

/**
 * Utility class for creating, clearing, and extracting refresh token cookies.
 *
 * Uses secure HTTP cookie flags to improve authentication security:
 * - httpOnly prevents JavaScript access (XSS protection)
 * - secure ensures HTTPS-only transmission
 * - SameSite Strict mitigates CSRF attacks
 *
 * The "__Host-" prefix ensures stricter browser security enforcement:
 * - Requires Secure flag
 * - Requires Path=/
 * - Disallows domain attribute
 */
public final class CookieUtil {
    // Secure cookie name using __Host- prefix for enhanced browser security enforcement
    private static final String COOKIE_NAME = "__Host-rt";
    // SameSite policy to prevent cross-site request forgery attacks
    private static final String SAME_SITE = "Strict";
    // Refresh token validity duration (7 days)
    private static final long COOKIE_MAX_AGE = 604800;

    private CookieUtil() {};

    /**
     * Creates a secure refresh token cookie for authentication sessions.
     */
    public static ResponseCookie createRefreshTokenCookie(String token) {
        return ResponseCookie.from(COOKIE_NAME, token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(COOKIE_MAX_AGE)
                .sameSite(SAME_SITE)
                .build();
    }

    /**
     * Clears refresh token cookie by setting an empty value and expiration to zero.
     */
    public static ResponseCookie clearRefreshTokenCookie() {
        return ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite(SAME_SITE)
                .build();
    }

    /**
     * Extracts refresh token value from request cookies.
     *
     * Returns null if cookie is not present.
     */
    public static String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        return Arrays.stream(cookies)
                .filter(c -> COOKIE_NAME.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}