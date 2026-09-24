package com.sencisobe.harbest.domain.service;

import org.springframework.stereotype.Service;

import com.sencisobe.harbest.domain.model.Water;

@Service

public class GrowthCalculator {

    //need to look where is this method called -> in registerwater
    public double calculateGrowthPoints(Water water, int streak) {
        double multiplier = 1 + Math.min(streak * 0.02, 0.5);
        return water.getDuration() * multiplier;
    }
}
