package com.notif.api.auth.application.service;

import com.notif.api.auth.api.dto.AuthenticatedUserResponse;
import com.notif.api.auth.application.dto.DeviceDto;
import com.notif.api.auth.application.dto.UserDeviceDto;
import com.notif.api.auth.domain.model.Device;
import com.notif.api.auth.domain.model.UserDevice;
import com.notif.api.auth.domain.repository.UserDeviceRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

/**
 * Service implementation for managing user devices.
 */
@Service
@RequiredArgsConstructor
public class UserDeviceServiceImpl implements UserDeviceService {
    private final UserDeviceRepository userDeviceRepository;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public UserDeviceDto registerLogin(AuthenticatedUserResponse user, DeviceDto device) {
        Device deviceProxy = entityManager.getReference(Device.class, device.getId());

        Optional<UserDevice> byUserIdAndDevice = userDeviceRepository.findByUserIdAndDevice(user.getId(), deviceProxy);
        if (byUserIdAndDevice.isPresent()) {
            UserDevice existingUserDevice = byUserIdAndDevice.get();
            existingUserDevice.setLastSeenAt(Instant.now());
            return mapUserDeviceToDto(existingUserDevice);
        }

        String nickname = user.getFirstName() + "'s " + device.getModel();

        UserDevice userDevice = UserDevice.builder()
                .userId(user.getId())
                .device(deviceProxy)
                .nickname(nickname)
                .build();

        UserDevice savedUserDevice = userDeviceRepository.save(userDevice);

        return mapUserDeviceToDto(savedUserDevice);
    }

    private UserDeviceDto mapUserDeviceToDto(UserDevice userDevice) {
        return UserDeviceDto.builder()
                .id(userDevice.getId())
                .userId(userDevice.getUserId())
                .deviceId(userDevice.getDevice().getId())
                .nickname(userDevice.getNickname())
                .userAgent(userDevice.getDevice().getUserAgent())
                .lastSeenAt(userDevice.getLastSeenAt())
                .build();
    }
}