package com.notif.api.auth.infrastructure.security;

import com.notif.api.user.api.dto.UserAuthDetails;
import lombok.*;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

/**
 * Spring Security adapter that wraps {@link UserAuthDetails}
 * and exposes it as a {@link UserDetails} implementation.
 *
 * Separates security-layer representation from persistence entity.
 */
public class NotifUserDetails implements UserDetails, CredentialsContainer {
    // Authenticated user snapshot
    private final UserAuthDetails user;
    // Local copy of password to allow credential erasure after authentication
    private String password;

    public NotifUserDetails(UserAuthDetails user) {
        this.user = user;
        this.password = user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRole().getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    @Override
    public void eraseCredentials() {
        // Clears sensitive credential data after authentication
        this.password = null;
    }

    // Exposes internal user ID for token generation or auditing
    public UUID getId() {
        return user.getId();
    }
}