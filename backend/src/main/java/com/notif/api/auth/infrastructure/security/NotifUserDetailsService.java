package com.notif.api.auth.infrastructure.security;

import com.notif.api.core.exception.BusinessException;
import com.notif.api.user.api.dto.UserAuthDetails;
import com.notif.api.user.infrastructure.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security UserDetailsService implementation that retrieves
 * authentication details from an external user service via UserClient.
 *
 * Acts as a bridge between the authentication provider and the user management system.
 */
@Service
@RequiredArgsConstructor
public class NotifUserDetailsService implements UserDetailsService {
    private final UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // Fetch user authentication details
            UserAuthDetails user = userClient.getUserAuthDetailsByEmail(username);

            // Converts domain user authentication data into a security principal representation.
            // This avoids exposing JPA-managed entities to the security context and prevents unintended
            // persistence-side effects.
            return new NotifUserDetails(user);
        } catch (BusinessException ex) {
            // Will catch USER_NOT_FOUND exception thrown by UserClient
            throw new UsernameNotFoundException(ex.getMessage());
        }
    }
}