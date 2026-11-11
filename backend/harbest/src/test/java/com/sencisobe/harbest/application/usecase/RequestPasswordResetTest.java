package com.sencisobe.harbest.application.usecase;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sencisobe.harbest.domain.model.PasswordResetToken;
import com.sencisobe.harbest.support.*;

public class RequestPasswordResetTest {

    private final FakeUserRepository users = new FakeUserRepository();
    private final FakePasswordResetTokenRepository tokens = new FakePasswordResetTokenRepository();
    private final FakeTokenHasher tokenHasher = new FakeTokenHasher();
    private final FakeEmailSender emails = new FakeEmailSender();
    private final FakeAuditLog audit = new FakeAuditLog();
    private final RequestPasswordReset requestReset =
            new RequestPasswordReset(users, tokens, tokenHasher, emails, audit);

    @BeforeEach
    void setUp() {
        new RegisterUser(users, new FakePasswordHasher(), audit).execute("a@x.com", "ana", "12345678");
        audit.events.clear();
    }

    @Test
    void sendsEmailToExistingAccountByEmail() {
        requestReset.execute("a@x.com");

        assertEquals(1, emails.sent.size());
        assertEquals("a@x.com", emails.sent.get(0).to());
        assertFalse(emails.lastToken().isBlank());
    }

    @Test
    void sendsEmailToExistingAccountByUsername() {
        requestReset.execute("  ANA ");

        assertEquals(1, emails.sent.size());
        assertEquals("a@x.com", emails.sent.get(0).to());
    }

    @Test
    void storesOnlyTheHashOfTheToken() {
        requestReset.execute("a@x.com");
        String raw = emails.lastToken();

        assertTrue(tokens.findByTokenHash(tokenHasher.hash(raw)).isPresent());
        assertTrue(tokens.findByTokenHash(raw).isEmpty());
    }

    @Test
    void tokenExpiresInTheFuture() {
        requestReset.execute("a@x.com");

        PasswordResetToken token = tokens.findByTokenHash(tokenHasher.hash(emails.lastToken())).orElseThrow();
        assertTrue(token.getExpiresAt().isAfter(Instant.now()));
        assertFalse(token.isUsed());
    }

    @Test
    void unknownAccountSendsNothingAndDoesNotThrow() {
        assertDoesNotThrow(() -> requestReset.execute("nadie@x.com"));
        assertDoesNotThrow(() -> requestReset.execute("nadie"));

        assertTrue(emails.sent.isEmpty());
        assertTrue(audit.events.isEmpty());
    }

    @Test
    void newRequestInvalidatesThePreviousToken() {
        requestReset.execute("a@x.com");
        String first = emails.lastToken();
        requestReset.execute("a@x.com");
        String second = emails.lastToken();

        assertNotEquals(first, second);
        assertTrue(tokens.findByTokenHash(tokenHasher.hash(first)).isEmpty());
        assertTrue(tokens.findByTokenHash(tokenHasher.hash(second)).isPresent());
    }

    @Test
    void recordsAuditEvent() {
        requestReset.execute("a@x.com");

        assertEquals(1, audit.events.size());
        assertEquals("PASSWORD_RESET_REQUESTED", audit.events.get(0).action());
        assertEquals(1L, audit.events.get(0).userId());
    }
}