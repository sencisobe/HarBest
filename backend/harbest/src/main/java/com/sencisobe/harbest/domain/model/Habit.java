package com.sencisobe.harbest.domain.model;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sencisobe.harbest.domain.exception.InvalidHabitDataException;

import java.time.LocalDate;
import java.util.ArrayList;

public class Habit {
        private static final Logger log = LoggerFactory.getLogger(Habit.class);

        private long userId;
        private Long id;
        private String name;
        private int dailyObjectiveTime;
        private  GrowthStage growthStage;
        private List<Water> waterHistory;
        private int streak;
        private LocalDate creationDate;
        private double totalExperience;

    public Habit(Long userId, String name, int dailyObjectiveTime){
    if (userId==null) throw new InvalidHabitDataException("userId is required");
        if (dailyObjectiveTime <= 0) {
        throw new InvalidHabitDataException("dailyObjectiveTime must be positive");
    }
        this.name = name;
        this.dailyObjectiveTime= dailyObjectiveTime;
        this.growthStage= GrowthStage.SPROUT;
        this.waterHistory= new ArrayList<>(); 
        this.streak=0;
        this.creationDate= LocalDate.now();
        this.totalExperience=0;

    }
    // For Persistency
    public Habit(Long userId, Long id, String name, int dailyObjectiveTime, GrowthStage growthStage,
                int streak, double totalExperience, List<Water> waterHistory, LocalDate creationDate) {
       this.userId = userId;
        this.id = id;
        this.name = name;
        this.dailyObjectiveTime = dailyObjectiveTime;
        this.growthStage = growthStage;
        this.streak = streak;
        this.totalExperience = totalExperience;
        this.waterHistory = new ArrayList<>(waterHistory);
        this.creationDate = creationDate;
}

    // METHODS 
    /**
     * Register how the habit was watered in a day and update the XP
     * @param water Daily Water to register
     * @param growthPoints points to sum accounting streak multiplier
     * 
     */
   // Habit.java
public void waterRegister(Water water, double growthPoints) {
    int totalTodayBefore = waterHistory.stream()
            .filter(w -> w.getDate().equals(water.getDate()))
            .mapToInt(Water::getDuration)
            .sum();

    Optional<Water> todayWater = waterHistory.stream()
            .filter(w -> w.getDate().equals(water.getDate()))
            .findFirst();

    if (todayWater.isPresent()) {
        todayWater.get().addDuration(water.getDuration());
    } else {
        waterHistory.add(water);
    }

    totalExperience += growthPoints;
    updateStreak(water, totalTodayBefore);
    updateGrowthStage();
}
public int minutesDoneOn(LocalDate date) {
    return waterHistory.stream()
            .filter(w -> w.getDate().equals(date))
            .mapToInt(Water::getDuration)
            .sum();
}
public boolean metObjectiveOn(LocalDate date) {
    int total = waterHistory.stream()
            .filter(w -> w.getDate().equals(date))
            .mapToInt(Water::getDuration)
            .sum();
    return total >= dailyObjectiveTime;
}

public int remainingMinutesOn(LocalDate date) {
    int total = waterHistory.stream()
            .filter(w -> w.getDate().equals(date))
            .mapToInt(Water::getDuration)
            .sum();
    return Math.max(0, dailyObjectiveTime - total);
}

public void updateStreak(Water water, int totalTodayBefore) {
    boolean alreadyMetObjectiveToday = totalTodayBefore >= dailyObjectiveTime;
    int totalTodayDuration = totalTodayBefore + water.getDuration();
    boolean metObjectiveNow = totalTodayDuration >= dailyObjectiveTime;

    log.debug("updateStreak: fecha={}, totalAntes={}, totalAhora={}, objetivo={}, yaCumplidoAntes={}, cumpleAhora={}",
            water.getDate(), totalTodayBefore, totalTodayDuration, dailyObjectiveTime,
            alreadyMetObjectiveToday, metObjectiveNow);

    if (metObjectiveNow && !alreadyMetObjectiveToday) {
        LocalDate yesterday = water.getDate().minusDays(1);
        boolean isFirstEver = waterHistory.stream().map(Water::getDate).distinct().count() <= 1;
boolean wateredYesterday = waterHistory.stream()
        .anyMatch(w -> w.getDate().equals(yesterday) && w.getDuration() >= dailyObjectiveTime);
        
        if (isFirstEver || wateredYesterday) {
            streak++;
            log.info("Streak incrementado a {}", streak);
        } else {
            streak = streak / 2;
            log.info("Racha rota, streak reducido a {}", streak);
        }
    }
}
        private void updateGrowthStage() {
    if (totalExperience >= dailyObjectiveTime * 30) {
        growthStage = GrowthStage.TREE;
    } else if (totalExperience >= dailyObjectiveTime * 7) {
        growthStage = GrowthStage.HALF_TREE;
    } else {
        growthStage = GrowthStage.SPROUT;
    }
}

    // GETTERS


public Long getUserId() {
     return userId;
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
public int getStreak(){
    return streak;
}

public List<Water> getWaterHistory() {
    return List.copyOf(waterHistory);
}

public LocalDate getCreationDate() {
    return creationDate;
}
public double getTotalExperience(){
    return totalExperience;
}

}
