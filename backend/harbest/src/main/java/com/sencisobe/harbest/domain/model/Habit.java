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
        private LocalDate creationDate;

    public Habit(String name, int dailyObtectiveTime){
        
        this.name = name;
        this.dailyObjectiveTime= dailyObtectiveTime;
        this.growthStage= growthStage.SPROUT;
        this.waterHistory= new ArrayList<>(); 
        this.creationDate= LocalDate.now();

    }
    public void waterRegister ( Water water){

        waterHistory.add(water);
        
    }
}
