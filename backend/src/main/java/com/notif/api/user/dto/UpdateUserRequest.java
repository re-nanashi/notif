package com.notif.api.user.dto;

import lombok.Data;

/**
 * Request DTO for updating a user's profile information.
 *
 * Only includes fields that are allowed to be updated by the user.
 * All fields are optional; only non-null fields will be applied.
 */
@Data
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
}