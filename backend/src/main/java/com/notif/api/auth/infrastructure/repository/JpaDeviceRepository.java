package com.notif.api.auth.infrastructure.repository;

import com.notif.api.auth.domain.model.Device;
import com.notif.api.auth.domain.repository.DeviceRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Spring Data JPA repository for Device, extending JpaRepository and DeviceRepository.
 */
public interface JpaDeviceRepository extends JpaRepository<Device, UUID>, DeviceRepository {
}