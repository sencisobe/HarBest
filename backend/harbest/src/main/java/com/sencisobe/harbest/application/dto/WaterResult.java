// application/dto/WaterResult.java
package com.sencisobe.harbest.application.dto;

import com.sencisobe.harbest.domain.model.Habit;

public class WaterResult {

    private final Habit habit;
    private final boolean objectiveMetToday;
    private final int remainingMinutesToday;

    public WaterResult(Habit habit, boolean objectiveMetToday, int remainingMinutesToday) {
        this.habit = habit;
        this.objectiveMetToday = objectiveMetToday;
        this.remainingMinutesToday = remainingMinutesToday;
    }

    public Habit getHabit() {
        return habit;
    }

    public boolean isObjectiveMetToday() {
        return objectiveMetToday;
    }

    public int getRemainingMinutesToday() {
        return remainingMinutesToday;
    }
}