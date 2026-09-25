package com.sencisobe.harbest.domain.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class HabitTest {

    @Test
    void diasConsecutivosIncrementanStreak() {
        Habit habit = new Habit("Leer", 30);
        LocalDate day1 = LocalDate.of(2026, 1, 1);
        LocalDate day2 = LocalDate.of(2026, 1, 2);
        LocalDate day3 = LocalDate.of(2026, 1, 3);

        habit.waterRegister(new Water(day1, 30), 30.0);
        habit.waterRegister(new Water(day2, 30), 30.0);
        habit.waterRegister(new Water(day3, 30), 30.0);

        assertEquals(3, habit.getStreak());
    }

    @Test
    void saltarUnDiaReduceStreakALaMitad() {
        Habit habit = new Habit("Leer", 30);
        LocalDate day1 = LocalDate.of(2026, 1, 1);
        LocalDate day2 = LocalDate.of(2026, 1, 2);
        LocalDate day4 = LocalDate.of(2026, 1, 4); // salta el día 3

        habit.waterRegister(new Water(day1, 30), 30.0); // streak = 1
        habit.waterRegister(new Water(day2, 30), 30.0); // streak = 2
        habit.waterRegister(new Water(day4, 30), 30.0); // no es consecutivo

        assertEquals(1, habit.getStreak()); // 2 / 2 = 1
    }

    @Test
    void variosRiegosMismoDiaSeSumanYCuentanUnaVezElStreak() {
        Habit habit = new Habit("Leer", 30);
        LocalDate day1 = LocalDate.of(2026, 1, 1);

        habit.waterRegister(new Water(day1, 15), 15.0); // no llega a 30, streak no sube
        habit.waterRegister(new Water(day1, 10), 10.0); // total hoy = 35, cruza el umbral
        habit.waterRegister(new Water(day1, 20), 20.0); // total hoy = 35, cruza el umbral

        assertEquals(1, habit.getStreak());
        assertEquals(1, habit.getWaterHistory().size()); // se fusionó en un solo Water
    }
      @Test
    void dayWaterIsNotEnoughForStreak() {
        Habit habit = new Habit("Leer", 30);
        LocalDate day1 = LocalDate.of(2026, 1, 1);

        habit.waterRegister(new Water(day1, 15), 15.0); // no llega a 30, streak no sube
        habit.waterRegister(new Water(day1, 10), 10.0); // total hoy = 35, cruza el umbral

        assertEquals(0, habit.getStreak());
        assertEquals(1, habit.getWaterHistory().size()); // se fusionó en un solo Water
    }
}