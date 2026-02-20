package com.notif.api.auth.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.notif.api.core.exception.CustomAuthenticationException;
import com.notif.api.core.exception.ErrorCode;
import com.notif.api.core.dto.ApiError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws ServletException, IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String errorCode;
        String errorDetails;

        if (authException instanceof CustomAuthenticationException customAuthException) {
            errorCode = customAuthException.getErrorCode().getValue();
            errorDetails = customAuthException.getMessage();
        } else {
            errorCode = ErrorCode.AUTHENTICATION_FAILED.getValue();
            errorDetails = "Authentication failed. Please log in again or try again later.";
        }

        ApiError error = ApiError.builder()
                .title(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .status(HttpStatus.UNAUTHORIZED.value())
                // TODO: All authentication errors, even token expired errors are being handled by this
                .error(errorCode)
                .detail(errorDetails)
                .timestamp(LocalDateTime.now())
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        mapper.writeValue(response.getOutputStream(), error);
    }
}