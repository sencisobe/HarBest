package com.sencisobe.harbest.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import com.sencisobe.harbest.domain.model.PasswordResetToken;
import com.sencisobe.harbest.domain.repository.PasswordResetTokenRepository;

public class FakePasswordResetTokenRepository implements PasswordResetTokenRepository {

    private final Map<Long, PasswordResetToken> store = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @Override
    public PasswordResetToken save(PasswordResetToken token) {
        Long id = token.getId() != null ? token.getId() : sequence.getAndIncrement();
        PasswordResetToken saved = new PasswordResetToken(
                id, token.getUserId(), token.getTokenHash(), token.getExpiresAt(), token.isUsed());
        store.put(id, saved);
        return saved;
    }

    @Override
    public Optional<PasswordResetToken> findByTokenHash(String tokenHash) {
        return store.values().stream().filter(t -> t.getTokenHash().equals(tokenHash)).findFirst();
    }

    @Override
    public void deleteByUserId(Long userId) {
        store.values().removeIf(t -> t.getUserId().equals(userId));
    }
}