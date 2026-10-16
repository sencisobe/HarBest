package com.sencisobe.harbest.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email or username is mandatory")
        String identifier,

        @NotBlank(message = "Password is mandatory")
        String password) {}