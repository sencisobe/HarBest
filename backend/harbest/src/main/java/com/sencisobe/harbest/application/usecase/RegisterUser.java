package com.sencisobe.harbest.application.usecase;

import org.springframework.stereotype.Service;

import com.sencisobe.harbest.domain.exception.EmailAlreadyUsedException;
import com.sencisobe.harbest.domain.model.User;
import com.sencisobe.harbest.domain.repository.UserRepository;
import com.sencisobe.harbest.domain.service.PasswordHasher;

@Service
public class RegisterUser {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public RegisterUser(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public User execute(String email, String rawPassword) {
        String cleanEmail = email.trim().toLowerCase();
        if (userRepository.existsByEmail(cleanEmail)) {
            throw new EmailAlreadyUsedException(cleanEmail);
        }
        return userRepository.save(new User(cleanEmail, passwordHasher.hash(rawPassword)));
    }
}