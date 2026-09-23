package com.sencisobe.harbest.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "waters")
public class WaterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private int duration;

    @ManyToOne
    @JoinColumn(name = "habit_id")
    private HabitEntity habit;

    protected WaterEntity() {
    }

    public WaterEntity(LocalDate date, int duration, HabitEntity habit) {
        this.date = date;
        this.duration = duration;
        this.habit = habit;
    }

   public LocalDate getDate() {
    return date;
}

public int getDuration() {
    return duration;
}

public HabitEntity getHabit() {
    return habit;
}
}