package com.sencisobe.harbest.infrastructure.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 3, max = 20) @Pattern(regexp = "^[A-Za-z0-9_]+$") String username,
        @NotBlank @Size(min = 8, max = 72) String password
 )
  {}
