package com.notif.api.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.notif.api.user.entity.Permission.*;

/**
 * Enum representing application roles (USER, ADMIN, MANAGER).
 * Each role is associated with a set of permissions.
 */
@RequiredArgsConstructor
public enum Role {
    // Regular user with no extra permissions
    USER(Collections.emptySet()),

    // Administrator with all admin + manager permissions
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_CREATE,
                    ADMIN_DELETE,
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_CREATE,
                    MANAGER_DELETE
            )
    ),

    // Manager role with manager-only permissions
    MANAGER(
            Set.of(
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_CREATE,
                    MANAGER_DELETE
            )
    );

    /**
     * The set of permissions assigned to role.
     */
    @Getter
    private final Set<Permission> permissions;

    /**
     * Converts this role and its permission into a list of Spring Security
     * {@link SimpleGrantedAuthority} objects.
     *
     * - Each permission is mapped as granted authority.
     * - The role itself is also added as "ROLE_{ROLE_NAME}"
     *
     * @return a list of Spring Security objects.
     */
    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        // Add role name itself as an authority
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }
}