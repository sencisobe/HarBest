// infrastructure/web/dto/WaterResponse.java
package com.sencisobe.harbest.infrastructure.web.dto;

import com.sencisobe.harbest.domain.model.Habit;

public class WaterResponse {

    private final HabitResponse habit;
    private final boolean objectiveMetToday;
    private final int remainingMinutesToday;

    public WaterResponse(Habit habit, boolean objectiveMetToday, int remainingMinutesToday) {
        this.habit = new HabitResponse(habit);
        this.objectiveMetToday = objectiveMetToday;
        this.remainingMinutesToday = remainingMinutesToday;
    }

    public HabitResponse getHabit() { return habit; }
    public boolean isObjectiveMetToday() { return objectiveMetToday; }
    public int getRemainingMinutesToday() { return remainingMinutesToday; }
}