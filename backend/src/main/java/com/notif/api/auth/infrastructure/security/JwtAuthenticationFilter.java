package com.notif.api.auth.infrastructure.security;

import com.notif.api.core.exception.CustomAuthenticationException;
import com.notif.api.core.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter is a Spring Security filter that intercepts incoming HTTP requests
 * to validate JWT tokens and set up the Spring Security context for authenticated users.
 *
 * All exceptions related to authentication (e.g., invalid, expired, or malformed JWTs)
 * are propagated to the AuthenticationEntryPoint, which handles the HTTP response and error formatting.
 * This keeps the filter focused on authentication logic rather than response handling.
 *
 * Additionally, user account-specific checks (e.g., disabled, locked, or expired accounts)
 * are performed here after extracting the username from the JWT. This ensures that even if a JWT
 * is valid, access is denied if the user's account status has changed since token issuance.
 * Exceptions are thrown to be handled by the AuthenticationEntryPoint, resulting in proper 401 Unauthorized
 * responses with contextual error information.
 *
 * This filter works together with Spring Security's filter chain and does not handle business-level
 * authorization; role or access checks should be enforced via Spring Security configuration or
 * method-level annotations.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final NotifUserDetailsService userDetailsService;
    private final UserAccessValidator userAccessValidator;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            // Missing token; return early
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String jwtToken = authHeader.substring(7);
            final String username = jwtTokenProvider.extractUsername(jwtToken);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                NotifUserDetails userDetails = (NotifUserDetails) userDetailsService.loadUserByUsername(username);
                // Validate user access
                userAccessValidator.validateUserAccess(userDetails);

                if (jwtTokenProvider.isTokenValid(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        // Throw JwtException as CustomAuthenticationException to be handled by JwtAuthEntryPoint with context
        } catch (JwtException ex) {
            String message;
            ErrorCode errorCode;

            switch (ex) {
                case ExpiredJwtException e -> {
                    message = "Your session has expired.";
                    errorCode = ErrorCode.AUTH_TOKEN_EXPIRED;
                }
                case MalformedJwtException e -> {
                    message = "Invalid authentication format.";
                    errorCode = ErrorCode.AUTH_TOKEN_MALFORMED;
                }
                case SignatureException e -> {
                    message = "Token signature validation failed.";
                    errorCode = ErrorCode.AUTH_TOKEN_SIGNATURE_INVALID;
                }
                default -> {
                    message = "Authentication token is invalid.";
                    errorCode = ErrorCode.AUTH_TOKEN_INVALID;
                }
            }

            handleAuthenticationException(request, response,
                    new CustomAuthenticationException(message, errorCode, ex)
            );
        } catch (AuthenticationException ex) {
            // This block catches CustomAuthenticationExceptions thrown in UserAccessValidator and other authentication
            // exceptions
            handleAuthenticationException(request, response, ex);
        } catch (Exception ex) {
            handleAuthenticationException(request, response,
                    new CustomAuthenticationException(
                            "Authentication failed. Please log in again or try again later.",
                            ErrorCode.AUTHENTICATION_FAILED,
                            ex
                    )
            );
        }
    }

    private void handleAuthenticationException(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {
        // Clear any partial authentication
        SecurityContextHolder.clearContext();

        // Delegate to the AuthenticationEntryPoint
        authenticationEntryPoint.commence(request, response, exception);
    }
}