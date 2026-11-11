package com.sencisobe.harbest.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.sencisobe.harbest.domain.model.PasswordResetToken;
import com.sencisobe.harbest.domain.repository.PasswordResetTokenRepository;
import com.sencisobe.harbest.infrastructure.persistence.entity.PasswordResetTokenEntity;

import jakarta.transaction.Transactional;


@Repository
public class PasswordResetTokenRepositoryImpl implements PasswordResetTokenRepository {

    private final PasswordResetTokenJpaRepository jpaRepository;

    public PasswordResetTokenRepositoryImpl(PasswordResetTokenJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PasswordResetToken save(PasswordResetToken token) {
        PasswordResetTokenEntity entity = new PasswordResetTokenEntity(
                token.getUserId(), token.getTokenHash(), token.getExpiresAt(), token.isUsed());
        entity.setId(token.getId());
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<PasswordResetToken> findByTokenHash(String tokenHash) {
        return jpaRepository.findByTokenHash(tokenHash).map(this::toDomain);
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        jpaRepository.deleteByUserId(userId);
    }

    private PasswordResetToken toDomain(PasswordResetTokenEntity e) {
        return new PasswordResetToken(e.getId(), e.getUserId(), e.getTokenHash(), e.getExpiresAt(), e.isUsed());
    }
}