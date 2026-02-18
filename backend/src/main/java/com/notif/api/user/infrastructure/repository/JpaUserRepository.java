package com.notif.api.user.infrastructure.repository;

import com.notif.api.user.domain.model.User;
import com.notif.api.user.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for managing {@link User} entities.
 */
@Repository
public interface JpaUserRepository extends JpaRepository<User, UUID>, UserRepository {
}