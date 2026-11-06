package com.sencisobe.harbest.domain.model;

import java.time.Instant;

public record AuditEvent(Long userId, String action, String detail, Instant at) {
    public AuditEvent(Long userId, String action, String detail) {
        this(userId, action, detail, Instant.now());
    }
}
