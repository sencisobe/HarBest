package com.sencisobe.harbest.domain.exception;

public class UsernameAlreadyUsedException extends RuntimeException {
    public UsernameAlreadyUsedException(String username) {
        super("Username already registered: " + username);
    }
}
