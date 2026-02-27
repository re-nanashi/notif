package com.notif.api.user.infrastructure.repository;

import com.notif.api.user.domain.model.TokenStatus;
import com.notif.api.user.domain.model.User;
import com.notif.api.user.domain.model.VerificationToken;
import com.notif.api.user.domain.repository.VerificationTokenRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for VerificationToken, extending JpaRepository and VerificationTokenRepository.
 */
@Repository
public interface JpaVerificationTokenRepository extends JpaRepository<VerificationToken, Long>, VerificationTokenRepository {
    @Modifying
    @Query("UPDATE VerificationToken t SET t.status = :newStatus WHERE t.user = :user AND t.status = :currentStatus")
    void voidPendingTokensByUser(@Param("user") User user,
                                 @Param("newStatus") TokenStatus newStatus,
                                 @Param("currentStatus") TokenStatus currentStatus);
}