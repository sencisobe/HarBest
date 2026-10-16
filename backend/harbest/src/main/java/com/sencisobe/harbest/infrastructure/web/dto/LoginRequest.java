package com.sencisobe.harbest.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String identifier,
        @NotBlank String password) {}
