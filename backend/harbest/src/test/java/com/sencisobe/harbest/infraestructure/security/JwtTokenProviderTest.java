package com.sencisobe.harbest.infraestructure.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Base64;

import org.junit.jupiter.api.Test;

import com.sencisobe.harbest.infrastructure.security.JwtTokenProvider;

import io.jsonwebtoken.JwtException;

public class JwtTokenProviderTest {

    private static final String SECRET = Base64.getEncoder()
            .encodeToString("0123456789abcdef0123456789abcdef".getBytes());

    @Test
    void roundTripReturnsSameUserId() {
        JwtTokenProvider provider = new JwtTokenProvider(SECRET, 60_000);
        assertEquals(5L, provider.extractUserId(provider.generate(5L)));
    }

    @Test
    void rejectsExpiredToken() {
        JwtTokenProvider provider = new JwtTokenProvider(SECRET, -1_000);
        String token = provider.generate(5L);
        assertThrows(JwtException.class, () -> provider.extractUserId(token));
    }

    @Test
    void rejectsTamperedToken() {
        JwtTokenProvider provider = new JwtTokenProvider(SECRET, 60_000);
        String token = provider.generate(5L);
        String tampered = token.substring(0, token.length() - 2) + "xx";
        assertThrows(JwtException.class, () -> provider.extractUserId(tampered));
    }

    @Test
    void rejectsTokenSignedWithOtherKey() {
        String otherSecret = Base64.getEncoder()
                .encodeToString("ffffffffffffffffffffffffffffffff".getBytes());
        String token = new JwtTokenProvider(otherSecret, 60_000).generate(5L);
        JwtTokenProvider provider = new JwtTokenProvider(SECRET, 60_000);
        assertThrows(JwtException.class, () -> provider.extractUserId(token));
    }
}
