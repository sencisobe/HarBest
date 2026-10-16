package com.sencisobe.harbest.application.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sencisobe.harbest.domain.exception.EmailAlreadyUsedException;
import com.sencisobe.harbest.domain.exception.UsernameAlreadyUsedException;
import com.sencisobe.harbest.domain.model.User;
import com.sencisobe.harbest.domain.repository.UserRepository;
import com.sencisobe.harbest.domain.service.PasswordHasher;

@Service
public class RegisterUser {

    private static final Logger log = LoggerFactory.getLogger(RegisterUser.class);

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public RegisterUser(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public User execute(String email,String username, String rawPassword) {
        String cleanEmail = email.trim().toLowerCase();
        String cleanUsername = username.trim().toLowerCase();

        if (userRepository.existsByEmail(cleanEmail)) {
            log.warn("Registro rechazado: email ya registrado");
            throw new EmailAlreadyUsedException(cleanEmail); 
        }
        if (userRepository.existsByUsername(cleanUsername)) {
            log.warn("Registro rechazado: username ya registrado");
            throw new UsernameAlreadyUsedException(cleanUsername);
        }

        User saved = userRepository.save(new User(cleanEmail, cleanUsername,passwordHasher.hash(rawPassword)));
        log.info("Usuario registrado: userId={}", saved.getId());
        return saved;
    }
}