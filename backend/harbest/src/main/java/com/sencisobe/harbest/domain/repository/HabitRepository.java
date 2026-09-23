package com.sencisobe.harbest.domain.repository;

import com.sencisobe.harbest.domain.model.Habit;
import java.util.List;
import java.util.Optional;

public interface HabitRepository {
    Habit save(Habit habit);
    Optional<Habit> findById(Long id);
    List<Habit> findAll();
}