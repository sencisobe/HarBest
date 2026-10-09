package com.sencisobe.harbest.domain.repository;

import com.sencisobe.harbest.domain.model.User;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}