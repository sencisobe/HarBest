package com.sencisobe.harbest.domain.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class HabitTest {

    private static final LocalDate D1 = LocalDate.of(2026, 1, 1);
    private static final LocalDate D2 = D1.plusDays(1);
    private static final LocalDate D3 = D1.plusDays(2);
    private static final LocalDate D4 = D1.plusDays(3);


    @Test
    void diasConsecutivosIncrementanStreak() {
        Habit habit = new Habit(1L,"Leer", 30);
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
        Habit habit = new Habit(1L,"Leer", 30);
        LocalDate day1 = LocalDate.of(2026, 1, 1);
        LocalDate day2 = LocalDate.of(2026, 1, 2);
        LocalDate day3 = LocalDate.of(2026, 1, 3);
        LocalDate day4 = LocalDate.of(2026, 1, 4); // salta el día 3

        LocalDate day6 = LocalDate.of(2026, 1, 6); // salta el día 3

        habit.waterRegister(new Water(day1, 30), 30.0); // streak = 1
        habit.waterRegister(new Water(day2, 30), 30.0); // streak = 2
        habit.waterRegister(new Water(day3, 30), 30.0); // streak = 2
        habit.waterRegister(new Water(day4, 30), 30.0); // no es consecutivo

        habit.waterRegister(new Water(day6, 30), 30.0); // no es consecutivo

        assertEquals(2, habit.getStreak()); // 4 / 2 = 2
    }

    @Test
    void variosRiegosMismoDiaSeSumanYCuentanUnaVezElStreak() {
        Habit habit = new Habit(1L,"Leer", 30);
        LocalDate day1 = LocalDate.of(2026, 1, 1);

        habit.waterRegister(new Water(day1, 15), 15.0); // no llega a 30, streak no sube
        habit.waterRegister(new Water(day1, 10), 10.0); // total hoy = 35, cruza el umbral
        habit.waterRegister(new Water(day1, 20), 20.0); // total hoy = 35, cruza el umbral

        assertEquals(1, habit.getStreak());
        assertEquals(1, habit.getWaterHistory().size()); // se fusionó en un solo Water
    }
      @Test
    void dayWaterIsNotEnoughForStreak() {
        Habit habit = new Habit(1L,"Leer", 30);
        LocalDate day1 = LocalDate.of(2026, 1, 1);

        habit.waterRegister(new Water(day1, 15), 15.0); // no llega a 30, streak no sube
        habit.waterRegister(new Water(day1, 10), 10.0); // total hoy = 25, 

        assertEquals(0, habit.getStreak());
        assertEquals(1, habit.getWaterHistory().size()); // se fusionó en un solo Water
    }

        @Test
    void diaParcialNoCuentaComoDiaAnteriorParaLaRacha() {
        Habit habit = new Habit(1L,"Leer", 30);

        habit.waterRegister(new Water(D1, 30), 30);
        habit.waterRegister(new Water(D2, 30), 30); // streak 2
        habit.waterRegister(new Water(D3, 10), 10); // dia parcial, no cumple
        habit.waterRegister(new Water(D4, 30), 30); // ayer no cumplio -> se rompe

        assertEquals(1, habit.getStreak());
    }
//Experience

    // ---- Experiencia ----
    @Test
    void experienciaSeAcumulaEntreRiegos() {
        Habit habit = new Habit(1L,"Leer", 30);

        habit.waterRegister(new Water(D1, 30), 30.0);
        habit.waterRegister(new Water(D2, 30), 31.0);

        assertEquals(61.0, habit.getTotalExperience(), 0.001);
    }

    @Test
    void experienciaSeSumaEnRiegosDelMismoDia() {
        Habit habit = new Habit(1L,"Leer", 30);

        habit.waterRegister(new Water(D1, 15), 15.0);
        habit.waterRegister(new Water(D1, 20), 20.0);

        assertEquals(35.0, habit.getTotalExperience(), 0.001);
        
    }

    @Test
    void riegoParcialTambienDaExperiencia() {
        Habit habit = new Habit(1L,"Leer", 30);

        habit.waterRegister(new Water(D1, 10), 10.0);

        assertEquals(10.0, habit.getTotalExperience(), 0.001);
    }
//Evolution

  @Test
    void justoDebajoDelUmbralSigueSiendoSprout() {
        Habit habit = new Habit(1L,"Leer", 30);

        habit.waterRegister(new Water(D1, 30), 209);

        assertEquals(GrowthStage.SPROUT, habit.getGrowthStage());
    }

    @Test
    void alcanzarUmbralPasaAHalfTree() {
        Habit habit = new Habit(1L,"Leer", 30);

        habit.waterRegister(new Water(D1, 30), 210);

        assertEquals(GrowthStage.HALF_TREE, habit.getGrowthStage());
    }

    @Test
    void justoDebajoDelUmbralDeArbolSigueSiendoHalfTree() {
        Habit habit = new Habit(1L,"Leer", 30);

        habit.waterRegister(new Water(D1, 30), 899);

        assertEquals(GrowthStage.HALF_TREE, habit.getGrowthStage());
    }

    @Test
    void alcanzarUmbralPasaATree() {
        Habit habit = new Habit(1L,"Leer", 30);

        habit.waterRegister(new Water(D1, 30), 900);

        assertEquals(GrowthStage.TREE, habit.getGrowthStage());
    }
}