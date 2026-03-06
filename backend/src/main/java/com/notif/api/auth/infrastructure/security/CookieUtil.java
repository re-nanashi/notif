package com.notif.api.auth.infrastructure.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

import java.util.Arrays;

/**
 * Utility class for creating, clearing, and extracting cookies.
 *
 * Uses secure HTTP cookie flags to improve authentication security:
 * - httpOnly prevents JavaScript access (XSS protection)
 * - secure ensures HTTPS-only transmission
 * - SameSite Strict mitigates CSRF attacks
 */
public final class CookieUtil {
    public static final String REFRESH_COOKIE_NAME = "refresh_token";
    public static final String DEVICE_COOKIE_NAME = "device_id";
    public static final String SAME_SITE = "Strict";
    // Refresh token validity duration (7 days)
    public static final long REFRESH_COOKIE_MAX_AGE = 604800;
    // Device cookie validity duration (1 year)
    public static final long DEVICE_COOKIE_MAX_AGE = 60 * 60 * 24 * 365;

    private CookieUtil() {};

    /**
     * Creates a secure refresh token cookie for authentication sessions.
     */
    public static ResponseCookie createRefreshTokenCookie(String tokenString) {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, tokenString)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(REFRESH_COOKIE_MAX_AGE)
                .sameSite(SAME_SITE)
                .build();
    }

    public static ResponseCookie createDeviceIdCookie(String deviceId) {
        return ResponseCookie.from(DEVICE_COOKIE_NAME, deviceId)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(DEVICE_COOKIE_MAX_AGE)
                .build();
    }

    /**
     * Clears refresh token cookie by setting an empty value and expiration to zero.
     */
    public static ResponseCookie clearRefreshTokenCookie() {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite(SAME_SITE)
                .build();
    }

    /**
     * Extracts cookie value from request cookies.
     *
     * Returns null if cookie is not present.
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        return Arrays.stream(cookies)
                .filter(c -> cookieName.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}