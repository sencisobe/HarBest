// application/usecase/ListHabits.java
package com.sencisobe.harbest.application.usecase;

import com.sencisobe.harbest.domain.model.Habit;
import com.sencisobe.harbest.domain.repository.HabitRepository;

import java.util.List;

import org.springframework.stereotype.Service;
@Service

public class ListHabits {

    private final HabitRepository habitRepository;

    public ListHabits(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public List<Habit> execute() {
        return habitRepository.findAll();
    }
}