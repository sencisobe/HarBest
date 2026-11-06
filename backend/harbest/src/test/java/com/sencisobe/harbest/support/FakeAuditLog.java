package com.sencisobe.harbest.support;

import java.util.ArrayList;
import java.util.List;

import com.sencisobe.harbest.domain.model.AuditEvent;
import com.sencisobe.harbest.domain.repository.AuditLog;

public class FakeAuditLog implements AuditLog {

    public final List<AuditEvent> events = new ArrayList<>();

    @Override
    public void record(AuditEvent event) {
        events.add(event);
    }
}