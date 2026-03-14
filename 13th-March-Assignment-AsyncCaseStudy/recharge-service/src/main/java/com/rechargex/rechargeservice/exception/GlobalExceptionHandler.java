package com.rechargex.rechargeservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "CONSTRAINT_VIOLATION", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "INVALID_INPUT", "Validation Failed", request.getRequestURI());
    }

    @ExceptionHandler(PlanNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePlanNotFound(PlanNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "PLAN_NOT_FOUND", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<Map<String, Object>> handlePaymentFailed(PaymentFailedException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.PAYMENT_REQUIRED, "PAYMENT_FAILED", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", ex.getMessage(), request.getRequestURI());
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String errorCode, String message, String path) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("status", status.value());
        errorDetails.put("errorCode", errorCode);
        errorDetails.put("message", message);
        errorDetails.put("path", path);
        return new ResponseEntity<>(errorDetails, status);
    }
}
