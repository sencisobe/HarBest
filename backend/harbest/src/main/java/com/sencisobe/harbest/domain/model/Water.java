package com.sencisobe.harbest.domain.model;

import java.time.LocalDate;

import com.sencisobe.harbest.domain.exception.InvalidWaterDataException;

public class Water {
    private LocalDate date;
    private int duration;

public Water(int duration) {
    if (duration <= 0) {
        throw new InvalidWaterDataException("Duration must be positive");
    }
    this.date = LocalDate.now();
    this.duration = duration;
}

public Water(LocalDate date, int duration) {
    if (duration <= 0) {
        throw new InvalidWaterDataException("Duration must be positive");
    }
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
