package com.sencisobe.harbest.application.usecase;

import org.springframework.stereotype.Service;

import com.sencisobe.harbest.domain.exception.InvalidCredentialsException;
import com.sencisobe.harbest.domain.model.User;
import com.sencisobe.harbest.domain.repository.UserRepository;
import com.sencisobe.harbest.domain.service.PasswordHasher;
import com.sencisobe.harbest.domain.service.TokenProvider;

@Service
public class LoginUser {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenProvider tokenProvider;

    public LoginUser(UserRepository userRepository, PasswordHasher passwordHasher, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenProvider = tokenProvider;
    }

    public String execute(String email, String rawPassword) {
        User user = userRepository.findByEmail(email.trim().toLowerCase())
                .filter(u -> passwordHasher.matches(rawPassword, u.getPasswordHash()))
                .orElseThrow(InvalidCredentialsException::new);
        return tokenProvider.generate(user.getId());
    }
}