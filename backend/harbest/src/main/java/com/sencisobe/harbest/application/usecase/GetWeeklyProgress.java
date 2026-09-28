// application/usecase/GetWeeklyProgress.java
package com.sencisobe.harbest.application.usecase;

import com.sencisobe.harbest.application.dto.DailyProgress;
import com.sencisobe.harbest.domain.model.Habit;
import com.sencisobe.harbest.domain.model.Water;
import com.sencisobe.harbest.domain.repository.HabitRepository;

import com.sencisobe.harbest.domain.exception.HabitNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class GetWeeklyProgress {

    private static final Logger log = LoggerFactory.getLogger(GetWeeklyProgress.class);

    private final HabitRepository habitRepository;

    public GetWeeklyProgress(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public List<DailyProgress> execute(Long habitId) {
        log.info("Consultando progreso semanal del hábito {}", habitId);

        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> {
                    log.warn("Progreso semanal: hábito {} no encontrado", habitId);
                    return new HabitNotFoundException(habitId);
                });

        LocalDate today = LocalDate.now();
        LocalDate from = today.minusDays(6);
        log.debug("Ventana semanal: {} -> {}, riegos en historial: {}",
                from, today, habit.getWaterHistory().size());

        Map<LocalDate, Integer> byDate = habit.getWaterHistory().stream()
                .filter(w -> !w.getDate().isBefore(from))
                .collect(Collectors.groupingBy(Water::getDate,
                        Collectors.summingInt(Water::getDuration)));

        log.debug("Riegos dentro de la ventana, por fecha: {}", byDate);

        List<DailyProgress> result = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> {
                    LocalDate date = today.minusDays(6 - i);
                    return new DailyProgress(date, byDate.getOrDefault(date, 0));
                })
                .toList();

        log.info("Progreso semanal del hábito {}: {} min en {} días con riego",
                habitId, byDate.values().stream().mapToInt(Integer::intValue).sum(), byDate.size());

        return result;
    }
}