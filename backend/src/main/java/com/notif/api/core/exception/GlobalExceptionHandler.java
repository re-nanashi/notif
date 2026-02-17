package com.notif.api.core.exception;

import com.notif.api.core.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiError> handleResourceConflict(ResourceConflictException ex) {
        ApiError error = ApiError.builder()
                .title(HttpStatus.CONFLICT.getReasonPhrase())
                .status(HttpStatus.CONFLICT.value())
                .error(ex.getErrorCode().getValue())
                .detail(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiError error = ApiError.builder()
                .title(HttpStatus.NOT_FOUND.getReasonPhrase())
                .status(HttpStatus.NOT_FOUND.value())
                .error(ex.getErrorCode().getValue())
                .detail(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidation(ValidationException ex) {
        ApiError error = ApiError.builder()
                .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(ex.getErrorCode().getValue())
                .detail(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    // Handles @Valid errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<Map<String, String>> fieldErrors = new ArrayList<>();

        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    fieldErrors.add(Map.of("field", fieldName, "message", errorMessage));
                });

        ApiError error = ApiError.builder()
                .title("Invalid input")
                .status(HttpStatus.BAD_REQUEST.value())
                .error(ErrorCodes.VALIDATION_FAILED.getValue())
                .fieldErrors(fieldErrors)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    // Fallback for uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllExceptions(Exception ex) {
        ApiError error = ApiError.builder()
                .title(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(ErrorCodes.INTERNAL_SERVER_ERROR.getValue())
                .detail(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(error.getStatus()).body(error);
    }
}