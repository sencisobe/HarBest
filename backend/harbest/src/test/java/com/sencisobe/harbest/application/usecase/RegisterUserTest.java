package com.sencisobe.harbest.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.sencisobe.harbest.domain.exception.EmailAlreadyUsedException;
import com.sencisobe.harbest.domain.exception.UsernameAlreadyUsedException;
import com.sencisobe.harbest.domain.model.User;
import com.sencisobe.harbest.support.FakePasswordHasher;
import com.sencisobe.harbest.support.FakeUserRepository;

public class RegisterUserTest {

    private final FakeUserRepository users = new FakeUserRepository();
    private final RegisterUser registerUser = new RegisterUser(users, new FakePasswordHasher());

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
}