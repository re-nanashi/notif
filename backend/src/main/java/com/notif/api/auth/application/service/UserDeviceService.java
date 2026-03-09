package com.notif.api.auth.application.service;

import com.notif.api.auth.api.dto.AuthenticatedUserResponse;
import com.notif.api.auth.application.dto.DeviceDto;
import com.notif.api.auth.application.dto.UserDeviceDto;

/**
 * Service interface for managing user devices.
 */
public interface UserDeviceService {
    UserDeviceDto registerLogin(AuthenticatedUserResponse user, DeviceDto devicedto);
}