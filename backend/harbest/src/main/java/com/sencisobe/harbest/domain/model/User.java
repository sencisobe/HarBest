package com.sencisobe.harbest.domain.model;

import java.time.LocalDate;

public class User {

    private Long id;
    private String email;
    private String passwordHash;
    private LocalDate createdAt;

    public User(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDate.now();
    }

    // Para persistencia
    public User(Long id, String email, String passwordHash, LocalDate createdAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public LocalDate getCreatedAt() { return createdAt; }
}