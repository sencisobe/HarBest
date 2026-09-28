// src/test/java/com/sencisobe/harbest/application/usecase/RegisterWaterTest.java
package com.sencisobe.harbest.application.usecase;

import com.sencisobe.harbest.domain.exception.HabitNotFoundException;
import com.sencisobe.harbest.domain.model.GrowthStage;
import com.sencisobe.harbest.domain.model.Habit;
import com.sencisobe.harbest.domain.model.Water;
import com.sencisobe.harbest.domain.service.GrowthCalculator;
import com.sencisobe.harbest.support.FakeHabitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RegisterWaterTest {

    private static final LocalDate DAY = LocalDate.of(2026, 1, 1);

    private FakeHabitRepository repo;
    private RegisterWater useCase;

    @BeforeEach
    void setUp() {
        repo = new FakeHabitRepository();
        useCase = new RegisterWater(repo, new GrowthCalculator());
    }

    private Habit savedHabit(int streak, double xp) {
        return repo.save(new Habit(1L, "Leer", 30, GrowthStage.SPROUT,
                streak, xp, new ArrayList<>(), DAY.minusDays(30)));
    }

    @Test
    void riegoSinRachaSumaLaDuracionComoXp() {
        savedHabit(0, 0.0);

        Habit result = useCase.execute(1L, new Water(DAY, 30));

        assertEquals(30.0, result.getTotalExperience(), 0.001);
    }

    @Test
    void laRachaActualAplicaMultiplicadorAlXp() {
        savedHabit(10, 0.0); // streak 10 -> x1.2

        Habit result = useCase.execute(1L, new Water(DAY, 30));

        assertEquals(36.0, result.getTotalExperience(), 0.001);
    }

    @Test
    void cumplirObjetivoIncrementaLaRacha() {
        savedHabit(0, 0.0);

        Habit result = useCase.execute(1L, new Water(DAY, 30));

        assertEquals(1, result.getStreak());
    }

    @Test
    void elResultadoQuedaGuardadoEnElRepositorio() {
        savedHabit(0, 0.0);

        useCase.execute(1L, new Water(DAY, 30));

        Habit stored = repo.findById(1L).orElseThrow();
        assertEquals(1, stored.getWaterHistory().size());
        assertEquals(30.0, stored.getTotalExperience(), 0.001);
    }

    @Test
    void superarElUmbralHaceEvolucionarElHabito() {
        savedHabit(0, 200.0); // HALF_TREE empieza en 210

        Habit result = useCase.execute(1L, new Water(DAY, 30)); // 230

        assertEquals(GrowthStage.HALF_TREE, result.getGrowthStage());
    }

    @Test
    void dosRiegosElMismoDiaSeFusionan() {
        savedHabit(0, 0.0);

        useCase.execute(1L, new Water(DAY, 15));
        Habit result = useCase.execute(1L, new Water(DAY, 20));

        assertEquals(1, result.getWaterHistory().size());
        assertEquals(35, result.getWaterHistory().get(0).getDuration());
        assertEquals(1, result.getStreak());
    }

    @Test
    void habitoInexistenteLanzaExcepcion() {
        assertThrows(HabitNotFoundException.class,
                () -> useCase.execute(999L, new Water(DAY, 30)));
    }
}