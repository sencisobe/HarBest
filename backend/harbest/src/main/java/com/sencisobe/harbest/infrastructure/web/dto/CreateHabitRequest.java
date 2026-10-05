// infrastructure/web/dto/CreateHabitRequest.java
package com.sencisobe.harbest.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateHabitRequest {

    @NotBlank(message = "The name is mandatory")
    private String name;

    @NotNull(message = "Daily objective time must be filled")
    @Positive(message = "Daily objective time can´t be negative")
    private Integer dailyObjectiveTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDailyObjectiveTime() {
        return dailyObjectiveTime;
    }

    public void setDailyObjectiveTime(Integer dailyObjectiveTime) {
        this.dailyObjectiveTime = dailyObjectiveTime;
    }
}