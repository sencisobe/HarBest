package com.sencisobe.harbest.domain.model;

import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

public class Habit {

        private int id;
        private String name;
        private int dailyObjectiveTime;
        private  GrowthStage growthStage;
        private List<Water> waterHistory;
        private int streak;
        private LocalDate creationDate;
        private double totalExperience;

    public Habit(String name, int dailyObtectiveTime){
        
        this.name = name;
        this.dailyObjectiveTime= dailyObtectiveTime;
        this.growthStage= GrowthStage.SPROUT;
        this.waterHistory= new ArrayList<>(); 
        this.streak=0;
        this.creationDate= LocalDate.now();
        this.totalExperience=0;

    }

    // METHODS 
    /**
     * Register how the habit was watered in a day and update the XP
     * @param water Daily Water to register
     * @param growthPoints points to sum accounting streak multiplier
     * 
     */
    public void waterRegister ( Water water,double growthPoints){
       waterHistory.add(water);
        updateStreak(water);
        totalExperience += growthPoints;
        updateGrowthStage();
    }
    
   /**
     * Checks if a habit changes from Stage 
     *   7 days  for half tree and 30 days for tree
     *  
     */
    private void updateGrowthStage() {
    if (totalExperience >= dailyObjectiveTime * 30) {
        growthStage = GrowthStage.TREE;
    } else if (totalExperience >= dailyObjectiveTime * 7) {
        growthStage = GrowthStage.HALF_TREE;
    } else {
        growthStage = GrowthStage.SPROUT;
    }
}


/**
 * Update streak , increments if it accomplish the daily objective else punish it by halfing the streak
 * @param water Daily Water to register
 * 
 */
public void updateStreak(Water water){
    LocalDate lastWateredDate = waterHistory.size() > 1 ? waterHistory.get(waterHistory.size()-2).getDate() : null;
    
    boolean isConsecutiveDay = lastWateredDate != null
        && water.getDate().equals(lastWateredDate.plusDays(1));

    boolean metObjective = water.getDuration() >= dailyObjectiveTime;

    
    if (metObjective && (lastWateredDate == null || isConsecutiveDay)) {
        streak++;
    } else if (!metObjective) {
        streak = streak /2 ; 
    }
}
    // GETTERS
public int getId() {
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
