package com.sencisobe.harbest.domain.repository;

import java.util.Optional;

import com.sencisobe.harbest.domain.model.PasswordResetToken;

public interface PasswordResetTokenRepository {
    PasswordResetToken save(PasswordResetToken token);
    Optional<PasswordResetToken> findByTokenHash(String tokenHash);
    void deleteByUserId(Long userId);
}
