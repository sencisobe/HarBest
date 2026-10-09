package com.sencisobe.harbest.domain.service;

public interface TokenProvider {
    String generate(Long userId);
}
