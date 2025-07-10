package com.ticket_management_system.auth_service.auth_service.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank @Size(max=80) String name, @Email String email, @NotBlank String password, String companyName) {}
