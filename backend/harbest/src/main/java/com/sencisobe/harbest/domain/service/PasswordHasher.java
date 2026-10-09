package com.sencisobe.harbest.domain.service;

public interface PasswordHasher {
    String hash(String rawPassword);
    boolean matches(String rawPassword, String hash);
}

