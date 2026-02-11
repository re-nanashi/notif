package com.notif.api.common.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomValidationFieldError {
    private String field;
    private String message;
}