package com.sencisobe.harbest.support;

import com.sencisobe.harbest.domain.service.PasswordHasher;

public class FakePasswordHasher implements PasswordHasher {
    @Override public String hash(String raw) { return "hash:" + raw; }
    @Override public boolean matches(String raw, String hash) { return hash.equals("hash:" + raw); }
}
