package com.sencisobe.harbest.application.dto;

import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
        @NotBlank(message = "Email or username is mandatory")
        String identifier) {}
