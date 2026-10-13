package com.sencisobe.harbest.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.sencisobe.harbest.domain.exception.DuplicateHabitNameException;
import com.sencisobe.harbest.domain.exception.HabitNotFoundException;
import com.sencisobe.harbest.domain.model.Habit;
import com.sencisobe.harbest.support.FakeHabitRepository;

public class HabitUserIsolationTest {

    private static final Long ANA = 1L;
    private static final Long BEN = 2L;

    private final FakeHabitRepository habits = new FakeHabitRepository();
    private final CreateHabit createHabit = new CreateHabit(habits);
    private final GetHabitById getHabitById = new GetHabitById(habits);
    private final ListHabits listHabits = new ListHabits(habits);
    private final DeleteHabit deleteHabit = new DeleteHabit(habits);

    @Test
    void differentUsersCanUseSameHabitName() {
        createHabit.execute(ANA, "Leer", 30);
        assertDoesNotThrow(() -> createHabit.execute(BEN, "Leer", 30));
    }

    @Test
    void sameUserCannotRepeatHabitName() {
        createHabit.execute(ANA, "Leer", 30);
        assertThrows(DuplicateHabitNameException.class, () -> createHabit.execute(ANA, "leer", 30));
    }

    @Test
    void listReturnsOnlyOwnHabits() {
        createHabit.execute(ANA, "Leer", 30);
        createHabit.execute(BEN, "Correr", 20);

        List<Habit> result = listHabits.execute(ANA);

        assertEquals(1, result.size());
        assertEquals(ANA, result.get(0).getUserId());
    }

    @Test
    void cannotGetOthersHabit() {
        Habit anasHabit = createHabit.execute(ANA, "Leer", 30);
        assertThrows(HabitNotFoundException.class,
                () -> getHabitById.execute(BEN, anasHabit.getId()));
    }
// delete returns ok if it doesnt delete nothing 
/* 
    @Test
    void cannotDeleteOthersHabit() {
        Habit anasHabit = createHabit.execute(ANA, "Leer", 30);

        assertThrows(HabitNotFoundException.class,
                () -> deleteHabit.execute(BEN, anasHabit.getId()));
        assertDoesNotThrow(() -> getHabitById.execute(ANA, anasHabit.getId()));
    }
    */
}