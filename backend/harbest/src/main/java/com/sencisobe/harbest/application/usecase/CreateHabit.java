package com.sencisobe.harbest.application.usecase;

import org.springframework.stereotype.Service;

import com.sencisobe.harbest.domain.exception.DuplicateHabitNameException;
import com.sencisobe.harbest.domain.model.Habit;
import com.sencisobe.harbest.domain.repository.HabitRepository;

@Service

public class CreateHabit {

    private final HabitRepository habitRepository;

    public CreateHabit(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

public Habit execute(String name, int dailyObjectiveTime) {
    String cleanName = name.trim();
    if (habitRepository.existsByName(cleanName)) {
        throw new DuplicateHabitNameException(cleanName);
    }
    Habit habit = new Habit(cleanName, dailyObjectiveTime);
    return habitRepository.save(habit);
}
    
}