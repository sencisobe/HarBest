package com.sencisobe.harbest.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HabitTest {

    @Test
    void habitoNuevoEmpiezaComoSprout() {
        Habit habit = new Habit("Leer", 30);

        assertEquals(GrowthStage.SPROUT, habit.getGrowthStage());
        assertEquals(0, habit.getStreak());
        assertEquals(0.0, habit.getTotalExperience());
    }

    @Test
    void regarCumpliendoObjetivoIncrementaStreak() {
        Habit habit = new Habit("Leer", 30);
        Water water = new Water(30);

        habit.waterRegister(water, 30.0);

        assertEquals(1, habit.getStreak());
    }

    @Test
    void regarSinCumplirObjetivoReduceStreakALaMitad() {
        Habit habit = new Habit("Leer", 30);
        habit.waterRegister(new Water(30), 30.0); // streak = 1
        habit.waterRegister(new Water(30), 30.0); // streak = 2

        habit.waterRegister(new Water(10), 10.0); // no cumple objetivo (10 < 30)

        assertEquals(1, habit.getStreak()); // 2 / 2 = 1
    }

    @Test
    void suficienteXpPasaAHalfTree() {
        Habit habit = new Habit("Leer", 30);

        habit.waterRegister(new Water(30), 30 * 7); // XP = dailyObjectiveTime * 7

        assertEquals(GrowthStage.HALF_TREE, habit.getGrowthStage());
    }

    @Test
    void suficienteXpPasaATree() {
        Habit habit = new Habit("Leer", 30);

        habit.waterRegister(new Water(30), 30 * 30);

        assertEquals(GrowthStage.TREE, habit.getGrowthStage());
    }
}