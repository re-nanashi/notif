package com.notif.api.auth.domain.model;

import com.notif.api.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a user login session tied to a specific device.
 *
 * Tracks session status, activity, expiration, and revocation.
 * Used for authentication, logout, and security enforcement.
 */
@Entity
@Table(
        indexes = {
                @Index(columnList = "user_id, status"),
                @Index(columnList = "device_id, status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session extends BaseEntity {
    // Session duration constants in days
    public static final int MAX_LIFETIME = 365;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne(targetEntity = Device.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", referencedColumnName = "id", nullable = false)
    private Device device;

    @Column(name = "ip_address")
    private String ipAddress;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status = SessionStatus.ACTIVE;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Column(name = "revoked_reason")
    @Enumerated(EnumType.STRING)
    private SessionRevokedReason revokedReason;

    @Builder.Default
    @Column(name = "last_activity_at", nullable = false)
    private Instant lastActivityAt = Instant.now();

    @Column(name= "expires_at", nullable = false)
    private Instant expiresAt;
}