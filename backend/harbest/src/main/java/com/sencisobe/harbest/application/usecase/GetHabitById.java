// application/usecase/GetHabitById.java
package com.sencisobe.harbest.application.usecase;

import com.sencisobe.harbest.domain.model.Habit;
import com.sencisobe.harbest.domain.repository.HabitRepository;
import org.springframework.stereotype.Service;

@Service
public class GetHabitById {

    private final HabitRepository habitRepository;

    public GetHabitById(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public Habit execute(Long habitId) {
        return habitRepository.findById(habitId)
                .orElseThrow(() -> new IllegalArgumentException("Habit not found"));
    }
}