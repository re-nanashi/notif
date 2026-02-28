package com.notif.api.core.config;

import com.notif.api.auth.infrastructure.security.JwtAuthenticationEntryPoint;
import com.notif.api.auth.infrastructure.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.notif.api.user.domain.model.Role.ADMIN;
import static com.notif.api.user.domain.model.Role.MANAGER;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationManager authenticationManager;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    @Value("${api.prefix}")
    private String apiBasePath;

    private final String[] WHITE_LIST_URL = {
            "/api/v1/auth/test",
            "/api/v1/auth/login",
            "/api/v1/auth/refresh",
            "/api/v1/auth/register",
            "/api/v1/auth/confirm-registration",
            "/api/v1/auth/resend-confirmation",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (not needed for stateless JWT)
                .csrf(csrf -> csrf.disable())
                .anonymous(AbstractHttpConfigurer::disable) // Disables anonymous authentication
                // Stateless session
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Configure unauthorized handling
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
                // Configure endpoint authorization
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        // Role-based endpoints
                        .requestMatchers(apiBasePath + "/admin/**").hasRole(ADMIN.name())
                        .requestMatchers(apiBasePath + "/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())
                        // All other endpoints require authentication
                        .anyRequest()
                        .authenticated()
                )
                // Set custom authentication provider
                .authenticationManager(authenticationManager)
                // Add JWT filter before Spring Security's default filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // TODO: Configure logout

        return http.build();
    }
}