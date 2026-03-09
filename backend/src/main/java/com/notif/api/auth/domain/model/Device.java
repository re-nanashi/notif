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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Builder.Default
    @Column(name = "device_id", nullable = false, unique = true)
    private UUID deviceId = UUID.randomUUID();

    @Column(nullable = false)
    private String type;

    private String name;
    private String model;
    private String browser;
    private String os;

    @Column(name = "user_agent", nullable = false)
    private String userAgent;

    @Builder.Default
    @Column(name = "last_seen_at", nullable = false)
    private Instant lastSeenAt = Instant.now();
}