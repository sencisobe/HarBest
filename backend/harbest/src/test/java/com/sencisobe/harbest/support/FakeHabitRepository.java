// src/test/java/com/sencisobe/harbest/support/FakeHabitRepository.java
package com.sencisobe.harbest.support;

import com.sencisobe.harbest.domain.model.Habit;
import com.sencisobe.harbest.domain.repository.HabitRepository;

import java.util.*;

public class FakeHabitRepository implements HabitRepository {

    private final Map<Long, Habit> store = new HashMap<>();

    @Override
    public Habit save(Habit habit) {
        store.put(habit.getId(), habit);
        return habit;
    }

    @Override
    public Optional<Habit> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Habit> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }
}