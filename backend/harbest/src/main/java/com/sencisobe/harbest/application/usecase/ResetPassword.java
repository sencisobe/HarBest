package com.sencisobe.harbest.application.usecase;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sencisobe.harbest.domain.exception.InvalidResetTokenException;
import com.sencisobe.harbest.domain.model.AuditEvent;
import com.sencisobe.harbest.domain.model.PasswordResetToken;
import com.sencisobe.harbest.domain.model.User;
import com.sencisobe.harbest.domain.repository.AuditLog;
import com.sencisobe.harbest.domain.repository.PasswordResetTokenRepository;
import com.sencisobe.harbest.domain.repository.UserRepository;
import com.sencisobe.harbest.domain.service.PasswordHasher;
import com.sencisobe.harbest.domain.service.TokenHasher;

@Service
public class ResetPassword {

    private static final Logger log = LoggerFactory.getLogger(ResetPassword.class);

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final TokenHasher tokenHasher;
    private final PasswordHasher passwordHasher;
    private final AuditLog auditLog;

    public ResetPassword(UserRepository userRepository, PasswordResetTokenRepository tokenRepository,
                         TokenHasher tokenHasher, PasswordHasher passwordHasher, AuditLog auditLog) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.tokenHasher = tokenHasher;
        this.passwordHasher = passwordHasher;
        this.auditLog = auditLog;
    }

    public void execute(String rawToken, String newPassword) {
        PasswordResetToken token = tokenRepository.findByTokenHash(tokenHasher.hash(rawToken))
                .filter(t -> t.isUsableAt(Instant.now()))
                .orElseThrow(() -> {
                    log.warn("Password reset rejected: invalid or expired token");
                    return new InvalidResetTokenException();
                });

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(InvalidResetTokenException::new);

        user.changePasswordHash(passwordHasher.hash(newPassword));
        userRepository.save(user);

        token.markUsed();
        tokenRepository.save(token);

        log.info("Password reset completed: userId={}", user.getId());
        auditLog.record(new AuditEvent(user.getId(), "PASSWORD_RESET", null));
    }
}