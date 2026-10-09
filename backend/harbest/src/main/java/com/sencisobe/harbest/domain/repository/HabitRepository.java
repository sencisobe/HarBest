package com.sencisobe.harbest.domain.repository;

import com.sencisobe.harbest.domain.model.Habit;
import java.util.List;
import java.util.Optional;

public interface HabitRepository {
    Habit save(Habit habit);
    Optional<Habit> findByUserIdAndId(Long userId, Long id);
    List<Habit> findAllByUserId(Long userId);
    void deleteByUserIdAndId(Long userId, Long id);
    boolean existsByUserIdAndName(Long userId, String name);
}