package com.notif.api.user.domain.model;

import com.notif.api.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

import static com.notif.api.core.constants.AppConstants.*;

// Must link back to User
// Will be created after registration
// will expire within 24 hours
// unique, randomly generated
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationToken extends BaseEntity {
    public static final int EXPIRATION = MINUTES_PER_HOUR * HOURS_PER_DAY; // 24 hours in minutes

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Enumerated(EnumType.STRING)
    private TokenStatus status;

    public boolean isTokenExpired() {
        return expirationDate.isBefore(LocalDateTime.now());
    }
}