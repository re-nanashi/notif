package com.notif.api.auth.application.service;

import com.notif.api.auth.application.dto.DeviceDto;
import com.notif.api.auth.domain.model.Device;
import com.notif.api.auth.domain.repository.DeviceRepository;
import com.notif.api.core.utils.Util;
import lombok.RequiredArgsConstructor;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Service implementation for managing devices.
 */
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;
    private final UserAgentAnalyzer userAgentAnalyzer;

    /**
     * Register a new device or return an existing device from the DB.
     * - If the device is unknown (cookie missing or invalid), a new device row is created in the DB,
     *   and a new deviceId is generated (used for future requests via cookie).
     * - If the device is already known (cookie matches a DB record), the lastSeenAt timestamp is updated.
     *   The device entity is returned, and the existing cookie/deviceId can still be sent back to the client.
     */
    @Override
    @Transactional
    public DeviceDto registerDevice(String cookieDeviceId, String userAgent) {
        // Parse the deviceId from the cookie if present, ignoring it if malformed.
        // This ensures we only use a valid UUID for looking up an existing device.
        UUID deviceId = null;
        if (!Util.isNullOrBlank(cookieDeviceId)) {
            try {
                deviceId = UUID.fromString(cookieDeviceId);
            } catch (IllegalArgumentException ignored) {}
        }

        Optional<Device> byDeviceId = deviceId != null
                ? deviceRepository.findByDeviceId(deviceId)
                : Optional.empty();

        if (byDeviceId.isPresent()) {
            Device existingDevice = byDeviceId.get();
            existingDevice.setLastSeenAt(Instant.now());
            return mapDeviceToDto(existingDevice);
        }

        UserAgent agent = userAgentAnalyzer.parse(userAgent);

        String type = agent.getValue(UserAgent.DEVICE_CLASS);
        String model = agent.getValue(UserAgent.DEVICE_NAME);
        String os = agent.getValue(UserAgent.OPERATING_SYSTEM_NAME);
        String browser = agent.getValue(UserAgent.AGENT_NAME);

        boolean missingInfo = Util.isNullOrBlank(browser) || Util.isNullOrBlank(os);
        String deviceName = missingInfo
                ? "Unknown device"
                : browser + " on " + os;

        Device newDevice = Device.builder()
                .type(type)
                .name(deviceName)
                .model(model)
                .os(os)
                .browser(browser)
                .userAgent(userAgent)
                .build();

        Device savedDevice = deviceRepository.save(newDevice);

        return mapDeviceToDto(savedDevice);
    }

    /**
     * Converts a Device entity to a DeviceDto for external use.
     */
    private DeviceDto mapDeviceToDto(Device device) {
        return DeviceDto.builder()
                .id(device.getId())
                .deviceId(device.getDeviceId())
                .name(device.getName())
                .lastSeenAt(device.getLastSeenAt())
                .build();
    }
}