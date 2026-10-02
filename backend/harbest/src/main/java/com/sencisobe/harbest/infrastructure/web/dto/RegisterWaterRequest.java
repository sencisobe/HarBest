// infrastructure/web/dto/RegisterWaterRequest.java
package com.sencisobe.harbest.infrastructure.web.dto;

import jakarta.validation.constraints.Positive;

public class RegisterWaterRequest {

     @Positive(message = "El duracion debe ser positiva")
    private int duration;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}