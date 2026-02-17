package com.notif.api.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiError {
    // TODO: type, instance
    private String title;                                           // Brief error title
    private int status;                                             // HTTP status code
    private String error;                                           // Short description (e.g., "USER_NOT_FOUND")

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String detail;                                          // Error detail

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object fieldErrors;                                     // List of detailed error messages

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;                                // Timestamp when error happened
}