package com.notif.api.auth.infrastructure.repository;

import com.notif.api.auth.domain.model.UserDevice;
import com.notif.api.auth.domain.repository.UserDeviceRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Spring Data JPA repository for UserDevice, extending JpaRepository and UserDeviceRepository.
 */
public interface JpaUserDeviceRepository extends JpaRepository<UserDevice, UUID>, UserDeviceRepository {
}