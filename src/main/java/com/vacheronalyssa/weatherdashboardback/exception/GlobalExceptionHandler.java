package com.vacheronalyssa.weatherdashboardback.exception;

import com.vacheronalyssa.weatherdashboardback.dto.error.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> details = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            details.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return build(HttpStatus.BAD_REQUEST, "Validation des donnees echouee", request.getRequestURI(), details);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, String> details = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(violation -> details.put(
                violation.getPropertyPath().toString(),
                violation.getMessage())
        );

        return build(HttpStatus.BAD_REQUEST, "Validation des parametres echouee", request.getRequestURI(), details);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), Map.of());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalState(IllegalStateException ex, HttpServletRequest request) {
        return build(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), request.getRequestURI(), Map.of());
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<ApiErrorResponse> handleRestClientResponseException(
            RestClientResponseException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        if (ex.getStatusCode().is4xxClientError()) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
        }

        return build(status, "Service meteo indisponible pour le moment", request.getRequestURI(), Map.of());
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceAccessException(ResourceAccessException ex, HttpServletRequest request) {
        return build(HttpStatus.SERVICE_UNAVAILABLE, "Service meteo indisponible pour le moment", request.getRequestURI(), Map.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne serveur", request.getRequestURI(), Map.of());
    }

    private ResponseEntity<ApiErrorResponse> build(
            HttpStatus status,
            String message,
            String path,
            Map<String, String> details
    ) {
        ApiErrorResponse body = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                details
        );

        return ResponseEntity.status(status).body(body);
    }
}
