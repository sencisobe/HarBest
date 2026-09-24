// application/dto/DailyProgress.java
package com.sencisobe.harbest.application.dto;

import java.time.LocalDate;

public class DailyProgress {

    private final LocalDate date;
    private final int duration;

    public DailyProgress(LocalDate date, int duration) {
        this.date = date;
        this.duration = duration;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getDuration() {
        return duration;
    }
}