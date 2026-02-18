package com.notif.api.user.infrastructure.repository;

import com.notif.api.user.domain.model.VerificationToken;
import com.notif.api.user.domain.repository.VerificationTokenRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for VerificationToken, extending JpaRepository and VerificationTokenRepository.
 */
@Repository
public interface JpaVerificationTokenRepository extends JpaRepository<VerificationToken, Long>, VerificationTokenRepository {
}