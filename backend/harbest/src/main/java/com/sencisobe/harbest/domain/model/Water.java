package com.sencisobe.harbest.domain.model;

import java.time.LocalDate;

public class Water {
    private LocalDate date;
    private int duration;

    public Water(int duration){
        this.duration= duration;
        date= LocalDate.now();
    }
    public int getDuration() {
        return duration;
    }
    public LocalDate getDate(){
        return this.date;
    }
    public void setDuration(int add){
        
        this.duration=duration+add;
    }
}
