package com.sencisobe.harbest.support;

import com.sencisobe.harbest.domain.service.TokenProvider;

public class FakeTokenProvider implements TokenProvider {
    @Override public String generate(Long userId) { return "token-" + userId; }
}