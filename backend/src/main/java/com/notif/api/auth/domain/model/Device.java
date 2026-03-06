package com.notif.api.auth.domain.model;

import com.notif.api.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity representing a Device in the system.
 */
@Entity
@Table(indexes = {
        @Index(columnList = "device_id"),
        @Index(columnList = "fingerprint_hash")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "device_id", nullable = false, unique = true)
    private UUID deviceId = UUID.randomUUID();

    @Column(name = "fingerprint_hash", nullable = false, length = 64)
    private String fingerprintHash;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String browser;

    @Column(nullable = false)
    private String os;

    @Column(name = "user_agent", nullable = false)
    private String userAgent;

    @Column(name = "last_seen_at", nullable = false)
    private Instant lastSeenAt = Instant.now();
}