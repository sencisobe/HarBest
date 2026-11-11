package com.sencisobe.harbest.application.usecase;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sencisobe.harbest.domain.model.AuditEvent;
import com.sencisobe.harbest.domain.model.PasswordResetToken;
import com.sencisobe.harbest.domain.model.User;
import com.sencisobe.harbest.domain.repository.AuditLog;
import com.sencisobe.harbest.domain.repository.PasswordResetTokenRepository;
import com.sencisobe.harbest.domain.repository.UserRepository;
import com.sencisobe.harbest.domain.service.EmailSender;
import com.sencisobe.harbest.domain.service.TokenHasher;

@Service
public class RequestPasswordReset {

    private static final Logger log = LoggerFactory.getLogger(RequestPasswordReset.class);
    private static final Duration TOKEN_LIFETIME = Duration.ofMinutes(30);

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final TokenHasher tokenHasher;
    private final EmailSender emailSender;
    private final AuditLog auditLog;

    public RequestPasswordReset(UserRepository userRepository, PasswordResetTokenRepository tokenRepository,
                                TokenHasher tokenHasher, EmailSender emailSender, AuditLog auditLog) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.tokenHasher = tokenHasher;
        this.emailSender = emailSender;
        this.auditLog = auditLog;
    }

    public void execute(String identifier) {
        String clean = identifier.trim().toLowerCase();
        Optional<User> found = clean.contains("@")
                ? userRepository.findByEmail(clean)
                : userRepository.findByUsername(clean);

        if (found.isEmpty()) {
            log.warn("Password reset requested for unknown account");
            return;
        }

        User user = found.get();
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        tokenRepository.deleteByUserId(user.getId());
        tokenRepository.save(new PasswordResetToken(
                user.getId(), tokenHasher.hash(rawToken), Instant.now().plus(TOKEN_LIFETIME)));

        emailSender.sendPasswordReset(user.getEmail(), rawToken);
        log.info("Password reset requested: userId={}", user.getId());
        auditLog.record(new AuditEvent(user.getId(), "PASSWORD_RESET_REQUESTED", null));
    }
}
