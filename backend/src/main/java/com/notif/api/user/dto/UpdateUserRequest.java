package com.notif.api.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

/**
 * Request DTO used for updating a user's profile details.
 *
 * Only includes fields that are allowed to be updated by the user.
 */
@Data
public class UpdateUserRequest {
    // TODO: We should check if we really have an update request on either of the three
    private String firstName;
    private String lastName;
    @Email
    private String email;
}
