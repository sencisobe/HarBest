package com.sencisobe.harbest.infrastructure.persistence.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    private LocalDate createdAt;

    protected UserEntity() {}

    public UserEntity(String email, String passwordHash, LocalDate createdAt) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public LocalDate getCreatedAt() { return createdAt; }
    public void setId(Long id) { this.id = id; }
}