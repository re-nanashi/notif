package com.notif.api.user.dto;

import lombok.Data;

/**
 * DTO for transferring user profile data without sensitive fields like password.
 */
@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}