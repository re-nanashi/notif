package com.notif.api.auth.domain.repository;

import com.notif.api.auth.domain.model.Device;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Device entity, defining core CRUD operations and device-specific queries.
 */
public interface DeviceRepository {
    Device save(Device device);
    Optional<Device> findById(UUID id);
    List<Device> findAll();
    void deleteById(UUID id);
}