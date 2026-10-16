package com.sencisobe.harbest.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import com.sencisobe.harbest.domain.model.User;
import com.sencisobe.harbest.domain.repository.UserRepository;

public class FakeUserRepository implements UserRepository {

    private final Map<Long, User> store = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @Override
    public User save(User user) {
        Long id = user.getId() != null ? user.getId() : sequence.getAndIncrement();
        User saved = new User(id, user.getUsername(),user.getEmail(), user.getPasswordHash(), user.getCreatedAt());
        store.put(id, saved);
        return saved;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return store.values().stream().filter(u -> u.getEmail().equals(email)).findFirst();
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return store.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }
}