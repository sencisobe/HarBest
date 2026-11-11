package com.sencisobe.harbest.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank(message = "Token is mandatory")
        String token,

        @NotBlank(message = "Password is mandatory")
        @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
        String newPassword) {}
