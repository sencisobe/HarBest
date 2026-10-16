package com.sencisobe.harbest.domain.model;

import java.time.LocalDate;

public class User {

    private Long id;
    private String username;
    private String email;
    private String passwordHash;
    private LocalDate createdAt;

    public User(String username,String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDate.now();
    }

    // Para persistencia
    public User(Long id, String username, String email, String passwordHash, LocalDate createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getUsername() {return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public LocalDate getCreatedAt() { return createdAt; }
}