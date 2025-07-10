package com.ticket_management_system.auth_service.auth_service.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank
        @Size(max = 80)
        String name,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 128)   // BCrypt hash limit is 72, but raw pwd ≤128 is fine
        String password,

        @Size(max = 120)
        String companyName
) {}
