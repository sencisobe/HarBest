package com.sencisobe.harbest.infrastructure.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sencisobe.harbest.domain.service.EmailSender;

@Component
public class LoggingEmailSender implements EmailSender {

    private static final Logger log = LoggerFactory.getLogger(LoggingEmailSender.class);

    private final String frontendUrl;

    public LoggingEmailSender(@Value("${harbest.frontend-url}") String frontendUrl) {
        this.frontendUrl = frontendUrl;
    }

    @Override
    public void sendPasswordReset(String to, String rawToken) {
        log.info("[DEV] Password reset link: {}/#/reset-password/{}", frontendUrl, rawToken);
    }
}