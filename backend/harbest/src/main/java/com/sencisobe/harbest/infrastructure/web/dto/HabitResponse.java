// infrastructure/web/dto/HabitResponse.java
package com.sencisobe.harbest.infrastructure.web.dto;

import com.sencisobe.harbest.domain.model.GrowthStage;
import com.sencisobe.harbest.domain.model.Habit;

import java.time.LocalDate;

public class HabitResponse {

    private Long id;
    private String name;
    private int dailyObjectiveTime;
    private GrowthStage growthStage;
    private int streak;
    private double totalExperience;
    private LocalDate creationDate;

    public HabitResponse(Habit habit) {
        this.id = habit.getId();
        this.name = habit.getName();
        this.dailyObjectiveTime = habit.getDailyObjectiveTime();
        this.growthStage = habit.getGrowthStage();
        this.streak = habit.getStreak();
        this.totalExperience = habit.getTotalExperience();
        this.creationDate = habit.getCreationDate();
    }

    // getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public int getDailyObjectiveTime() { return dailyObjectiveTime; }
    public GrowthStage getGrowthStage() { return growthStage; }
    public int getStreak() { return streak; }
    public double getTotalExperience() { return totalExperience; }
    public LocalDate getCreationDate() { return creationDate; }
}