package com.sencisobe.harbest.support;

import com.sencisobe.harbest.domain.service.TokenHasher;

public class FakeTokenHasher implements TokenHasher {
    @Override
    public String hash(String rawToken) { return "tokenhash:" + rawToken; }
}
