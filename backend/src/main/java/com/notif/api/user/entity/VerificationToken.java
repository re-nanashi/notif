package com.notif.api.user.entity;

import jakarta.persistence.*;
import lombok.*;

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
public class VerificationToken {
    public static final int EXPIRATION = MINUTES_PER_HOUR * HOURS_PER_DAY; // 24 hours in minutes

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    public boolean isTokenExpired() {
        return expirationDate.isBefore(LocalDateTime.now());
    }
}