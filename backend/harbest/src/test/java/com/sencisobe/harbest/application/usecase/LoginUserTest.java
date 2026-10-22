package com.sencisobe.harbest.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sencisobe.harbest.domain.exception.InvalidCredentialsException;
import com.sencisobe.harbest.support.FakePasswordHasher;
import com.sencisobe.harbest.support.FakeTokenProvider;
import com.sencisobe.harbest.support.FakeUserRepository;

public class LoginUserTest {

    private final FakeUserRepository users = new FakeUserRepository();
    private final FakePasswordHasher hasher = new FakePasswordHasher();
    private final LoginUser loginUser = new LoginUser(users, hasher, new FakeTokenProvider());

    @BeforeEach
    void setUp() {
        new RegisterUser(users, hasher).execute("a@x.com", "ana", "12345678");
    }

    @Test
    void returnsTokenWithEmail() {
        assertEquals("token-1", loginUser.execute("a@x.com", "12345678").token());
    }

    @Test
    void returnsTokenWithUsername() {
        assertEquals("token-1", loginUser.execute("ana", "12345678").token());
    }
    @Test
    void loginIgnoresCaseAndSpaces() {
        assertEquals("token-1", loginUser.execute("  ANA ", "12345678").token());
        assertEquals("token-1", loginUser.execute("A@X.com", "12345678").token());
    }

    @Test
    void rejectsWrongPasswordWithEmail() {
        assertThrows(InvalidCredentialsException.class,
                () -> loginUser.execute("a@x.com", "incorrecta"));
    }

    @Test
    void rejectsWrongPasswordWithUsername() {
        assertThrows(InvalidCredentialsException.class,
                () -> loginUser.execute("ana", "incorrecta"));
    }

    @Test
    void rejectsUnknownEmailWithSameException() {
        assertThrows(InvalidCredentialsException.class,
                () -> loginUser.execute("nadie@x.com", "12345678"));
    }

    @Test
    void rejectsUnknownUsernameWithSameException() {
        assertThrows(InvalidCredentialsException.class,
                () -> loginUser.execute("nadie", "12345678"));
    }
}