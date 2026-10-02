// infrastructure/web/dto/CreateHabitRequest.java
package com.sencisobe.harbest.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateHabitRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotNull(message = "El tiempo diario es obligatorio")
    @Positive(message = "El tiempo diario debe ser positivo")
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