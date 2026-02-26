package com.notif.api.core.config;

import com.notif.api.auth.infrastructure.security.NotifUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Global application configuration class.
 *
 * Enables asynchronous processing and configures authentication-related beans for Spring Security.
 */
@Configuration
@EnableAsync
@RequiredArgsConstructor
public class ApplicationConfig {
    private final NotifUserDetailsService notifUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Configures the AuthenticationManager using DAO-based authentication.
     *
     * Uses a custom UserDetailsService for user lookup and PasswordEncoder for secure password verification.
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(notifUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }
}