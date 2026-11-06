package com.sencisobe.harbest.infrastructure.persistence.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_events")
public class AuditEventEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;          // nullable: login fallido sin usuario
    @Column(nullable = false)
    private String action;
    private String detail;
    private String ip;
    @Column(nullable = false)
    private Instant createdAt;

    protected AuditEventEntity() {}

    public AuditEventEntity(Long userId, String action, String detail, String ip, Instant createdAt) {
        this.userId = userId; this.action = action; this.detail = detail;
        this.ip = ip; this.createdAt = createdAt;
    }
}

