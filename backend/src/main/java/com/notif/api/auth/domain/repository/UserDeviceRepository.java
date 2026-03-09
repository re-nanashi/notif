package com.notif.api.auth.domain.repository;

import com.notif.api.auth.domain.model.Device;
import com.notif.api.auth.domain.model.UserDevice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for UserDevice entity, defining core CRUD operations and user device-specific queries.
 */
public interface UserDeviceRepository {
    UserDevice save(UserDevice userDevice);
    List<UserDevice> findByUserId(UUID userId);
    List<UserDevice> findByDevice(Device device);
    Optional<UserDevice> findByUserIdAndDevice(UUID userId, Device device);
}