package com.notif.api.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

/**
 * JPA entity representing a user in the system.
 *
 * Implements {@link UserDetails} for Spring Security authentication.
 * Contains login credentials, personal information, and unique email.
 *  TODO (Authentication): Implement timestamps
 */
@Entity
@Table(name = "_user", uniqueConstraints = {@UniqueConstraint(name = "uk_users_email", columnNames = "email")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails, CredentialsContainer {
    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Account + login
    @Column(nullable = false, columnDefinition = "citext")
    private String email;

    private String password; // null allowed for OAuth

    // Personal information
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}