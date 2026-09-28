package com.sencisobe.harbest.application.usecase;

import com.sencisobe.harbest.application.dto.DailyProgress;
import com.sencisobe.harbest.domain.model.GrowthStage;
import com.sencisobe.harbest.domain.model.Habit;
import com.sencisobe.harbest.domain.model.Water;
import com.sencisobe.harbest.domain.repository.HabitRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GetWeeklyProgressTest {

    private final LocalDate today = LocalDate.now();

    /** Repositorio en memoria, sin Spring ni base de datos. */
    static class FakeHabitRepository implements HabitRepository {
        private final Map<Long, Habit> store = new HashMap<>();

        @Override
        public Habit save(Habit habit) {
            store.put(habit.getId(), habit);
            return habit;
        }

        @Override
        public Optional<Habit> findById(Long id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public List<Habit> findAll() {
            return new ArrayList<>(store.values());
        }

        @Override
        public void deleteById(Long id) {
            store.remove(id);
        }
    }

    private GetWeeklyProgress useCaseWith(Water... waters) {
        FakeHabitRepository repo = new FakeHabitRepository();
        repo.save(new Habit(1L, "Leer", 30, GrowthStage.SPROUT, 0, 0.0,
                List.of(waters), today.minusDays(30)));
        return new GetWeeklyProgress(repo);
    }

    @Test
    void siempreDevuelveSieteDias() {
        List<DailyProgress> result = useCaseWith().execute(1L);

        assertEquals(7, result.size());
    }

    @Test
    void ordenCronologicoTerminandoHoy() {
        List<DailyProgress> result = useCaseWith().execute(1L);

        assertEquals(today.minusDays(6), result.get(0).getDate());
        assertEquals(today, result.get(6).getDate());
    }

    @Test
    void diasSinRiegoTienenDuracionCero() {
        List<DailyProgress> result = useCaseWith().execute(1L);

        assertTrue(result.stream().allMatch(d -> d.getDuration() == 0));
    }

    @Test
    void colocaCadaRiegoEnSuDia() {
        List<DailyProgress> result = useCaseWith(
                new Water(today, 30),
                new Water(today.minusDays(2), 20)
        ).execute(1L);

        assertEquals(30, result.get(6).getDuration()); // hoy
        assertEquals(20, result.get(4).getDuration()); // hace 2 dias
        assertEquals(0, result.get(5).getDuration());  // ayer, sin riego
    }

    @Test
    void incluyeElLimiteDeSeisDiasEIgnoraLoAnterior() {
        List<DailyProgress> result = useCaseWith(
                new Water(today.minusDays(6), 15), // dentro
                new Water(today.minusDays(7), 99)  // fuera
        ).execute(1L);

        assertEquals(15, result.get(0).getDuration());
        assertTrue(result.stream().noneMatch(d -> d.getDuration() == 99));
    }

    @Test
    void habitoInexistenteLanzaExcepcion() {
        GetWeeklyProgress useCase = new GetWeeklyProgress(new FakeHabitRepository());

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(999L));
    }
}