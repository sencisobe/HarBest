package com.sencisobe.harbest.domain.service;

import com.sencisobe.harbest.domain.model.Water;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GrowthCalculatorTest {

    private final GrowthCalculator calculator = new GrowthCalculator();

    @Test
    void noStreakMultiplierCorrect() {
        Water water = new Water(30);

        double points = calculator.calculateGrowthPoints(water, 0);

        assertEquals(30.0, points);
    }

    @Test
    void withStreakIsCorrectlyApplied() {
        Water water = new Water(30);

        double points = calculator.calculateGrowthPoints(water, 10);

        assertEquals(30 * 1.2, points, 0.001); // streak=10 → +20%
    }

    @Test
    void multiplierIsLimitedCorrectly() {
        Water water = new Water(30);

        double points = calculator.calculateGrowthPoints(water, 1000); // racha enorme

        assertEquals(30 * 1.5, points, 0.001); // tope en +50%
    }
}