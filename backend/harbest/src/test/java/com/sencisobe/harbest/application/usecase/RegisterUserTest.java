package com.sencisobe.harbest.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.sencisobe.harbest.domain.exception.EmailAlreadyUsedException;
import com.sencisobe.harbest.domain.exception.UsernameAlreadyUsedException;
import com.sencisobe.harbest.domain.model.User;
import com.sencisobe.harbest.support.FakeAuditLog;
import com.sencisobe.harbest.support.FakePasswordHasher;
import com.sencisobe.harbest.support.FakeUserRepository;

public class RegisterUserTest {

    private final FakeUserRepository users = new FakeUserRepository();
    private final FakeAuditLog audit = new FakeAuditLog();
    private final RegisterUser registerUser = new RegisterUser(users, new FakePasswordHasher(), audit);

    @Test
    void storesHashNotRawPassword() {
        User user = registerUser.execute("a@x.com", "ana", "12345678");
        assertEquals("hash:12345678", user.getPasswordHash());
    }

    @Test
    void normalizesEmail() {
        User user = registerUser.execute("  Ana@X.com ", "ana", "12345678");
        assertEquals("ana@x.com", user.getEmail());
    }

    @Test
    void normalizesUsername() {
        User user = registerUser.execute("a@x.com", "  Ana_99 ", "12345678");
        assertEquals("ana_99", user.getUsername());
    }

    @Test
    void rejectsDuplicateEmailIgnoringCase() {
        registerUser.execute("ana@x.com", "ana", "12345678");
        assertThrows(EmailAlreadyUsedException.class,
                () -> registerUser.execute("Ana@x.com", "otro", "12345678"));
    }

    @Test
    void rejectsDuplicateUsernameIgnoringCase() {
        registerUser.execute("a@x.com", "ana", "12345678");
        assertThrows(UsernameAlreadyUsedException.class,
                () -> registerUser.execute("b@x.com", "ANA", "12345678"));
    }

    @Test
    void recordsRegisterEventWithUserId() {
        User user = registerUser.execute("a@x.com", "ana", "12345678");

        assertEquals(1, audit.events.size());
        assertEquals("REGISTER", audit.events.get(0).action());
        assertEquals(user.getId(), audit.events.get(0).userId());
    }

    @Test
    void doesNotRecordEventWhenRegistrationFails() {
        registerUser.execute("a@x.com", "ana", "12345678");
        audit.events.clear();

        assertThrows(EmailAlreadyUsedException.class,
                () -> registerUser.execute("a@x.com", "otro", "12345678"));
        assertTrue(audit.events.isEmpty());
    }
}