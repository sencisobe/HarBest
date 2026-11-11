package com.sencisobe.harbest.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sencisobe.harbest.infrastructure.persistence.entity.PasswordResetTokenEntity;

public interface PasswordResetTokenJpaRepository extends JpaRepository<PasswordResetTokenEntity, Long> {
    Optional<PasswordResetTokenEntity> findByTokenHash(String tokenHash);
    void deleteByUserId(Long userId);
}
