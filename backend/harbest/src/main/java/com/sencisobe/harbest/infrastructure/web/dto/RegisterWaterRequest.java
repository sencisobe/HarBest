// infrastructure/web/dto/RegisterWaterRequest.java
package com.sencisobe.harbest.infrastructure.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class RegisterWaterRequest {

    @NotNull (message ="Minutes invested must be provided")
    @Positive(message = "Minutes invested can´t be negative")
    private Integer duration;

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}