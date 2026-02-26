package com.notif.api.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class responsible for password hashing strategy. PasswordEncoder is separated to avoid
 * circular dependencies when configuring authentication components.
 */
@Configuration
public class PasswordConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Returns BCryptPasswordEncoder bean for password hashing
        return new BCryptPasswordEncoder();
    }
}