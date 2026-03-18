package com.notif.api.auth.application.service;

import com.notif.api.auth.application.dto.DeviceDto;

import java.util.Optional;

/**
 * Service interface for managing devices used by the client.
 */
public interface DeviceService {
    DeviceDto registerDevice(String cookieDeviceId, String userAgent);
    Optional<DeviceDto> getDevice(String cookieDeviceId);
}