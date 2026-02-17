package com.notif.api.common.events;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
@Setter
public class UserRegisteredEvent extends ApplicationEvent {
    private UUID userId;
    private String userEmail;
    private String appUrl;

    public UserRegisteredEvent(UUID userId, String userEmail, String appUrl) {
        super(userId);

        this.userId = userId;
        this.userEmail = userEmail;
        this.appUrl = appUrl;
    }
}