// src/main/java/com/.../advice/GlobalExceptionHandler.java
package com.ticket_management_system.auth_service.auth_service.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestControllerAdvice                        // <-- magic annotation
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> handleBadCredentials(BadCredentialsException ex) {
        return Map.of("error", "Invalid credentials");
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(Exception ex) {
        return Map.of("error", "Validation failed", "details", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGeneric(Exception ex) {
        log.error("Unhandled error", ex);
        return Map.of("error", "Something went wrong");
    }
}
