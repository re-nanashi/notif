package com.notif.api.auth.domain.model;

import com.notif.api.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "user_device",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "device_id"}),
        indexes = {
                @Index(columnList = "user_id"),
                @Index(columnList = "device_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDevice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne(targetEntity = Device.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(nullable = false)
    private String nickname;

    // TODO: true for now (dev)
    @Builder.Default
    @Column(nullable = false)
    private boolean trusted = true;

    @Builder.Default
    @Column(name = "first_seen_at", nullable = false)
    private Instant firstSeenAt = Instant.now();

    @Builder.Default
    @Column(name = "last_seen_at", nullable = false)
    private Instant lastSeenAt = Instant.now();
}