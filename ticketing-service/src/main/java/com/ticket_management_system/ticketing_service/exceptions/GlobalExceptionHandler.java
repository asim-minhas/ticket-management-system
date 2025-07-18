// src/main/java/com/.../advice/GlobalExceptionHandler.java
package com.ticket_management_system.ticketing_service.exceptions;

import org.springframework.http.converter.HttpMessageNotReadableException;  // <— correct import
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
            MethodArgumentNotValidException.class,
            IllegalTransitionException.class

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
    @ExceptionHandler(TicketNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,String> handleNotFound(TicketNotFoundException ex) {
        return Map.of("error", "Ticket not found", "ticketId", ex.getTicketId());
    }
    /**
     * Handles JSON body parse errors (e.g. bad enum values in @RequestBody).
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidFormat(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidEx) {
            // Extract the JSON path field name, e.g. "ticketType"
            String field = invalidEx.getPath()
                    .stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("."));

            Class<?> targetType = invalidEx.getTargetType();
            if (targetType.isEnum()) {
                // Build a comma-separated list of allowed constants
                String allowed = Arrays.stream(targetType.getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));
                String badValue = invalidEx.getValue().toString();

                return Map.of(
                        "error",
                        String.format(
                                "Invalid value '%s' for field '%s'. Allowed values: [%s]",
                                badValue, field, allowed
                        )
                );
            }
        }

        // Fallback for other JSON parse errors
        return Map.of(
                "error", "Malformed JSON request",
                "details", ex.getMessage()
        );
    }

    /**
     * Handles enum conversion errors in @RequestParam or @PathVariable.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Class<?> requiredType = ex.getRequiredType();
        if (requiredType != null && requiredType.isEnum()) {
            // Build allowed constants list
            String allowed = Arrays.stream(requiredType.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            String badValue = Objects.toString(ex.getValue(), "");
            String name     = ex.getName();  // the name of the param or path variable

            return Map.of(
                    "error",
                    String.format(
                            "Invalid value '%s' for parameter '%s'. Allowed values: [%s]",
                            badValue, name, allowed
                    )
            );
        }

        // Fallback for other type mismatches
        return Map.of(
                "error", String.format("Parameter '%s' has invalid value '%s'", ex.getName(), ex.getValue())
        );
    }

}
