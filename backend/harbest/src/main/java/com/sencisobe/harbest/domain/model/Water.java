package com.sencisobe.harbest.domain.model;

import java.time.LocalDate;

public class Water {
    private LocalDate date;
    private int duration;

    public Water(int duration){
        date= LocalDate.now();
        this.duration= duration;
    }
       // For persistency
    public Water(LocalDate date, int duration) {
        this.date = date;
        this.duration = duration;
    }
    public int getDuration() {
        return duration;
    }
    public LocalDate getDate(){
        return this.date;
    }
    public void setDuration(int add){
        
        this.duration=add;
    }
    public void addDuration(int extra) {
    this.duration += extra;
    }
}
