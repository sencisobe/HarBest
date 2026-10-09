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
    public Optional<Habit> findByUserIdAndId(Long userId, Long id) {
        return Optional.ofNullable(store.get(id))
                .filter(h -> h.getUserId().equals(userId));
    }

    @Override
    public List<Habit> findAllByUserId(Long userId) {
        return store.values().stream()
                .filter(h -> h.getUserId().equals(userId))
                .toList();
    }

    @Override
    public void deleteByUserIdAndId(Long userId, Long id) {
        findByUserIdAndId(userId, id).ifPresent(h -> store.remove(id));
    }

    @Override
    public boolean existsByUserIdAndName(Long userId, String name) {
        return store.values().stream()
                .anyMatch(h -> h.getUserId().equals(userId) && h.getName().equalsIgnoreCase(name));
    }
}