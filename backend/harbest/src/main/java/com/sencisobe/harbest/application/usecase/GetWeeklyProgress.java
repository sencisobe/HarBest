// application/usecase/GetWeeklyProgress.java
package com.sencisobe.harbest.application.usecase;

import com.sencisobe.harbest.application.dto.DailyProgress;
import com.sencisobe.harbest.domain.model.Habit;
import com.sencisobe.harbest.domain.model.Water;
import com.sencisobe.harbest.domain.repository.HabitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class GetWeeklyProgress {

    private final HabitRepository habitRepository;

    public GetWeeklyProgress(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public List<DailyProgress> execute(Long habitId) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new IllegalArgumentException("Habit not found"));

        LocalDate today = LocalDate.now();

        Map<LocalDate, Integer> byDate = habit.getWaterHistory().stream()
                .filter(w -> !w.getDate().isBefore(today.minusDays(6)))
                .collect(Collectors.groupingBy(Water::getDate,
                        Collectors.summingInt(Water::getDuration)));

        return IntStream.rangeClosed(0, 6)
                .mapToObj(i -> {
                    LocalDate date = today.minusDays(6 - i);
                    return new DailyProgress(date, byDate.getOrDefault(date, 0));
                })
                .toList();
    }
}