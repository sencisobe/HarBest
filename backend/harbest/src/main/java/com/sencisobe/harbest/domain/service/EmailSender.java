package com.sencisobe.harbest.domain.service;

public interface EmailSender {
    void sendPasswordReset(String to, String link);
}
