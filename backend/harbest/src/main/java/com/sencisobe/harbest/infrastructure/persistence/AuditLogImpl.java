package com.sencisobe.harbest.infrastructure.persistence;

import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sencisobe.harbest.domain.model.AuditEvent;
import com.sencisobe.harbest.domain.repository.AuditLog;
import com.sencisobe.harbest.infrastructure.persistence.entity.AuditEventEntity;

@Repository
public class AuditLogImpl implements AuditLog {
    private final AuditEventJpaRepository jpa;

    public AuditLogImpl(AuditEventJpaRepository jpa) { this.jpa = jpa; }

    @Override
    public void record(AuditEvent e) {
        jpa.save(new AuditEventEntity(e.userId(), e.action(), e.detail(), currentIp(), e.at()));
    }

    private String currentIp() {
        return RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes a
                ? a.getRequest().getRemoteAddr() : null;
    }
}
