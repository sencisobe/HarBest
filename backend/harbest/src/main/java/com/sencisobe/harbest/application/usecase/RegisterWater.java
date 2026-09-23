// application/usecase/RegisterWater.java
package com.sencisobe.harbest.application.usecase;

import com.sencisobe.harbest.domain.model.Habit;
import com.sencisobe.harbest.domain.model.Water;
import com.sencisobe.harbest.domain.repository.HabitRepository;
import com.sencisobe.harbest.domain.service.GrowthCalculator;

public class RegisterWater {

    private final HabitRepository habitRepository;
    private final GrowthCalculator growthCalculator;

    public RegisterWater(HabitRepository habitRepository, GrowthCalculator growthCalculator) {
        this.habitRepository = habitRepository;
        this.growthCalculator = growthCalculator;
    }

    public Habit execute(Long habitId, Water water) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new IllegalArgumentException("Habit not found"));

        double growthPoints = growthCalculator.calculateGrowthPoints(water, habit.getStreak());
        habit.waterRegister(water, growthPoints);

        return habitRepository.save(habit);
    }
}