package com.sencisobe.harbest.support;

import java.util.ArrayList;
import java.util.List;

import com.sencisobe.harbest.domain.service.EmailSender;

public class FakeEmailSender implements EmailSender {

    public record Sent(String to, String token) {}

    public final List<Sent> sent = new ArrayList<>();

    @Override
    public void sendPasswordReset(String to, String rawToken) {
        sent.add(new Sent(to, rawToken));
    }

    public String lastToken() {
        return sent.get(sent.size() - 1).token();
    }
}