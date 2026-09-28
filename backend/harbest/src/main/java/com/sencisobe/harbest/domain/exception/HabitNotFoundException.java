// domain/exception/HabitNotFoundException.java
package com.sencisobe.harbest.domain.exception;

public class HabitNotFoundException extends RuntimeException {

    public HabitNotFoundException(Long habitId) {
        super("Habit " + habitId + " not found");
    }
}