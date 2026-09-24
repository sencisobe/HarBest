package com.sencisobe.harbest.infrastructure.persistence.entity;

import com.sencisobe.harbest.domain.model.GrowthStage;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "habits")
public class HabitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int dailyObjectiveTime;

    @Enumerated(EnumType.STRING)
    private GrowthStage growthStage;

    private int streak;
    private double totalExperience;
    private LocalDate creationDate;

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WaterEntity> waterHistory = new ArrayList<>();

    protected HabitEntity() {
        // constructor vacío requerido por JPA
    }

    public HabitEntity(String name, int dailyObjectiveTime, GrowthStage growthStage,
                        int streak, double totalExperience, LocalDate creationDate) {
        this.name = name;
        this.dailyObjectiveTime = dailyObjectiveTime;
        this.growthStage = growthStage;
        this.streak = streak;
        this.totalExperience = totalExperience;
        this.creationDate = creationDate;
    }

   public Long getId() {
    return id;
}

public String getName() {
    return name;
}

public int getDailyObjectiveTime() {
    return dailyObjectiveTime;
}

public GrowthStage getGrowthStage() {
    return growthStage;
}

public int getStreak() {
    return streak;
}

public double getTotalExperience() {
    return totalExperience;
}

public List<WaterEntity> getWaterHistory() {
    return waterHistory;
}

public LocalDate getCreationDate() {
    return creationDate;
}

public void setId(Long id) {
    this.id = id;
}
}