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

    public Habit(String name, int dailyObtectiveTime){
        
        this.name = name;
        this.dailyObjectiveTime= dailyObtectiveTime;
        this.growthStage= GrowthStage.SPROUT;
        this.waterHistory= new ArrayList<>(); 
        this.streak=0;
        this.creationDate= LocalDate.now();

    }
    public void waterRegister ( Water water){
        Water waterAlt= water;
        //need to see how constancy
        // and extreme number of hours account
        waterHistory.add(waterAlt);
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

public void updateStreak(Water water){
    LocalDate lastWateredDate = waterHistory.size() > 1 ? waterHistory.get(waterHistory.size()-2).getDate() : null;
}
}
