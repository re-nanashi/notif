package com.notif.api.common.exception;

import com.notif.api.common.mapper.ErrorMapper;
import com.notif.api.common.response.ApiError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private ErrorMapper errorMapper;

    // Handles custom exceptions
    @ExceptionHandler({ResourceNotFoundException.class, AlreadyExistsException.class, ResourceConflictException.class})
    public ResponseEntity<ApiError> handleCustomExceptions(RuntimeException ex) {
        ApiError apiError = errorMapper.mapException(ex);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    // Handles common exceptions
    @ExceptionHandler({IllegalStateException.class, BadCredentialsException.class})
    public ResponseEntity<ApiError> handleCommonExceptions(RuntimeException ex) {
        ApiError apiError = errorMapper.mapException(ex);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    // Handles validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> validationErrors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
                    } else {
                        return error.getDefaultMessage();
                    }
                })
                .collect(Collectors.toList());

        ApiError apiError = errorMapper.mapException(ex, validationErrors);

        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    // Fallback for uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllExceptions(Exception ex) {
        ApiError apiError = errorMapper.mapException(ex);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}