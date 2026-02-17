package com.notif.api.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiValidationError {
    private String title;                                     // Brief error title
    private int status;                                       // HTTP status code
    private String error;                                     // Short description (e.g., "USER_NOT_FOUND")
    private List<CustomValidationFieldError> fieldErrors;     // List of detailed error messages

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
}
