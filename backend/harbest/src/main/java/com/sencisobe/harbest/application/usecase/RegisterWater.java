// application/usecase/RegisterWater.java
package com.sencisobe.harbest.application.usecase;

import org.springframework.stereotype.Service;

import com.sencisobe.harbest.application.dto.WaterResult;
import com.sencisobe.harbest.domain.exception.HabitNotFoundException;
import com.sencisobe.harbest.domain.model.Habit;
import com.sencisobe.harbest.domain.model.Water;
import com.sencisobe.harbest.domain.repository.HabitRepository;
import com.sencisobe.harbest.domain.service.GrowthCalculator;

@Service

public class RegisterWater {

    private final HabitRepository habitRepository;
    private final GrowthCalculator growthCalculator;

    public RegisterWater(HabitRepository habitRepository, GrowthCalculator growthCalculator) {
        this.habitRepository = habitRepository;
        this.growthCalculator = growthCalculator;
    }

public WaterResult execute(Long habitId, Water water) {
    Habit habit = habitRepository.findById(habitId)
            .orElseThrow(() -> new HabitNotFoundException(habitId));

    double growthPoints = growthCalculator.calculateGrowthPoints(water, habit.getStreak());
    habit.waterRegister(water, growthPoints);
    Habit saved = habitRepository.save(habit);

    boolean objectiveMet = saved.metObjectiveOn(water.getDate());
    int remaining = saved.remainingMinutesOn(water.getDate());

    return new WaterResult(saved, objectiveMet, remaining);
}
}