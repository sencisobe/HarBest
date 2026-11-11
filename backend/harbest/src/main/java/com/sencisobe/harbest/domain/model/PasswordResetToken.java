package com.sencisobe.harbest.domain.model;

import java.time.Instant;

public class PasswordResetToken {
    private Long id;
    private Long userId;
    private String tokenHash;
    private Instant expiresAt;
    private boolean used;

    public PasswordResetToken(Long userId, String tokenHash, Instant expiresAt) {
        this.userId = userId;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.used = false;
    }

    // Para persistencia
    public PasswordResetToken(Long id, Long userId, String tokenHash, Instant expiresAt, boolean used) {
        this.id = id;
        this.userId = userId;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.used = used;
    }

    public boolean isUsableAt(Instant now) {
        return !used && expiresAt.isAfter(now);
    }

    public void markUsed() { this.used = true; }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getTokenHash() { return tokenHash; }
    public Instant getExpiresAt() { return expiresAt; }
    public boolean isUsed() { return used; }
}