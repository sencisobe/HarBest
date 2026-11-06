package com.sencisobe.harbest.domain.repository;

import com.sencisobe.harbest.domain.model.AuditEvent;

public interface AuditLog {
    void record(AuditEvent event);
}
