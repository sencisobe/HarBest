package com.sencisobe.harbest.application.usecase;

import com.sencisobe.harbest.domain.repository.HabitRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteHabit {

    private final HabitRepository habitRepository;

    public DeleteHabit(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public void execute(Long habitId) {
        habitRepository.deleteById(habitId);
    }
}