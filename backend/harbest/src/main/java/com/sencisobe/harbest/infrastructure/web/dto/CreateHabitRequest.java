// infrastructure/web/dto/CreateHabitRequest.java
package com.sencisobe.harbest.infrastructure.web.dto;

public class CreateHabitRequest {

    private String name;
    private int dailyObjectiveTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDailyObjectiveTime() {
        return dailyObjectiveTime;
    }

    public void setDailyObjectiveTime(int dailyObjectiveTime) {
        this.dailyObjectiveTime = dailyObjectiveTime;
    }
}