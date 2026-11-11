package com.sencisobe.harbest.application.usecase;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sencisobe.harbest.domain.exception.InvalidCredentialsException;
import com.sencisobe.harbest.domain.exception.InvalidResetTokenException;
import com.sencisobe.harbest.domain.model.PasswordResetToken;
import com.sencisobe.harbest.support.*;

public class ResetPasswordTest {

    private final FakeUserRepository users = new FakeUserRepository();
    private final FakePasswordResetTokenRepository tokens = new FakePasswordResetTokenRepository();
    private final FakeTokenHasher tokenHasher = new FakeTokenHasher();
    private final FakePasswordHasher passwordHasher = new FakePasswordHasher();
    private final FakeEmailSender emails = new FakeEmailSender();
    private final FakeAuditLog audit = new FakeAuditLog();

    private final ResetPassword resetPassword =
            new ResetPassword(users, tokens, tokenHasher, passwordHasher, audit);
    private final LoginUser loginUser =
            new LoginUser(users, passwordHasher, new FakeTokenProvider(), audit);

    private String rawToken;

    @BeforeEach
    void setUp() {
        new RegisterUser(users, passwordHasher, audit).execute("a@x.com", "ana", "12345678");
        new RequestPasswordReset(users, tokens, tokenHasher, emails, audit).execute("a@x.com");
        rawToken = emails.lastToken();
        audit.events.clear();
    }

    @Test
    void newPasswordWorksAndOldOneDoesNot() {
        resetPassword.execute(rawToken, "nuevaClave1");

        assertEquals("token-1", loginUser.execute("ana", "nuevaClave1").token());
        assertThrows(InvalidCredentialsException.class, () -> loginUser.execute("ana", "12345678"));
    }

    @Test
    void storesHashNotRawPassword() {
        resetPassword.execute(rawToken, "nuevaClave1");

        assertEquals("hash:nuevaClave1", users.findById(1L).orElseThrow().getPasswordHash());
    }

    @Test
    void tokenIsMarkedAsUsed() {
        resetPassword.execute(rawToken, "nuevaClave1");

        assertTrue(tokens.findByTokenHash(tokenHasher.hash(rawToken)).orElseThrow().isUsed());
    }

    @Test
    void rejectsAlreadyUsedToken() {
        resetPassword.execute(rawToken, "nuevaClave1");

        assertThrows(InvalidResetTokenException.class,
                () -> resetPassword.execute(rawToken, "otraClave22"));
        assertEquals("token-1", loginUser.execute("ana", "nuevaClave1").token());
    }

    @Test
    void rejectsUnknownToken() {
        assertThrows(InvalidResetTokenException.class,
                () -> resetPassword.execute("token-inventado", "nuevaClave1"));
    }

    @Test
    void rejectsExpiredToken() {
        tokens.save(new PasswordResetToken(1L, tokenHasher.hash("caducado"), Instant.now().minusSeconds(60)));

        assertThrows(InvalidResetTokenException.class,
                () -> resetPassword.execute("caducado", "nuevaClave1"));
    }

    @Test
    void rejectedResetDoesNotChangePassword() {
        assertThrows(InvalidResetTokenException.class,
                () -> resetPassword.execute("token-inventado", "nuevaClave1"));

        assertEquals("token-1", loginUser.execute("ana", "12345678").token());
    }

    @Test
    void recordsAuditEventOnSuccess() {
        resetPassword.execute(rawToken, "nuevaClave1");

        assertEquals(1, audit.events.size());
        assertEquals("PASSWORD_RESET", audit.events.get(0).action());
        assertEquals(1L, audit.events.get(0).userId());
    }

    @Test
    void doesNotRecordAuditEventOnFailure() {
        assertThrows(InvalidResetTokenException.class,
                () -> resetPassword.execute("token-inventado", "nuevaClave1"));

        assertTrue(audit.events.isEmpty());
    }
}