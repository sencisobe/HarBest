package com.sencisobe.harbest.application.usecase;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sencisobe.harbest.application.dto.LoginResult;
import com.sencisobe.harbest.domain.exception.InvalidCredentialsException;
import com.sencisobe.harbest.domain.model.User;
import com.sencisobe.harbest.domain.repository.UserRepository;
import com.sencisobe.harbest.domain.service.PasswordHasher;
import com.sencisobe.harbest.domain.service.TokenProvider;

@Service
public class LoginUser {

    private static final Logger log = LoggerFactory.getLogger(LoginUser.class);

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenProvider tokenProvider;

    public LoginUser(UserRepository userRepository, PasswordHasher passwordHasher, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenProvider = tokenProvider;
    }

    public LoginResult execute(String identifier, String rawPassword) {
        String clean = identifier.trim().toLowerCase();
        Optional<User> found = clean.contains("@")
                ? userRepository.findByEmail(clean)
                : userRepository.findByUsername(clean);
        User user = found
                .filter(u -> passwordHasher.matches(rawPassword, u.getPasswordHash()))
                .orElseThrow(() -> { log.warn("Login fallido"); return new InvalidCredentialsException(); });

        log.info("Login correcto: userId={}", user.getId());
        return new LoginResult(tokenProvider.generate(user.getId()), user.getUsername());
    }
}