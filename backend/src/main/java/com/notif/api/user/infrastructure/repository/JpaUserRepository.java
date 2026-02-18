package com.notif.api.user.infrastructure.repository;

import com.notif.api.user.domain.model.User;
import com.notif.api.user.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for User, extending JpaRepository and UserRepository.
 */
@Repository
public interface JpaUserRepository extends JpaRepository<User, UUID>, UserRepository {
}