// application/usecase/RegisterWater.java
package com.sencisobe.harbest.application.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sencisobe.harbest.application.dto.WaterResult;
import com.sencisobe.harbest.domain.exception.HabitNotFoundException;
import com.sencisobe.harbest.domain.model.Habit;
import com.sencisobe.harbest.domain.model.Water;
import com.sencisobe.harbest.domain.repository.HabitRepository;
import com.sencisobe.harbest.domain.service.GrowthCalculator;

@Service
public class RegisterWater {

    private static final Logger log = LoggerFactory.getLogger(RegisterWater.class);

    private final HabitRepository habitRepository;
    private final GrowthCalculator growthCalculator;

    public RegisterWater(HabitRepository habitRepository, GrowthCalculator growthCalculator) {
        this.habitRepository = habitRepository;
        this.growthCalculator = growthCalculator;
    }

    public WaterResult execute(Long userId, Long habitId, Water water) {
        Habit habit = habitRepository.findByUserIdAndId(userId, habitId)
                .orElseThrow(() -> {
                    log.warn("Riego rechazado: hábito no encontrado o ajeno, userId={}, habitId={}", userId, habitId);
                    return new HabitNotFoundException(habitId);
                });

        double growthPoints = growthCalculator.calculateGrowthPoints(water, habit.getStreak());
        habit.waterRegister(water, growthPoints);
        Habit saved = habitRepository.save(habit);

        boolean objectiveMet = saved.metObjectiveOn(water.getDate());
        int remaining = saved.remainingMinutesOn(water.getDate());

        log.info("Riego registrado: userId={}, habitId={}, minutos={}, objetivoCumplido={}",
                userId, habitId, water.getDuration(), objectiveMet);

        return new WaterResult(saved, objectiveMet, remaining);
    }
}