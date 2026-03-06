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
    Optional<Device> findByDeviceId(UUID deviceId);         // cookie lookup
    Optional<Device> findByFingerprintHash(String hash);    // fingerprint lookup
    void deleteById(UUID id);
}