package com.sencisobe.harbest.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sencisobe.harbest.infrastructure.persistence.entity.AuditEventEntity;

public interface AuditEventJpaRepository extends JpaRepository<AuditEventEntity, Long> {

}
