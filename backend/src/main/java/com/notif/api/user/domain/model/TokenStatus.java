package com.notif.api.user.domain.model;

public enum TokenStatus {
    PENDING,        // Created byt not yet used
    VERIFIED,       // Successfully used for verification
    VOIDED,         // Manually invalidated
    EXPIRED         // Past its validity duration (will be deleted through batch processing)
}
