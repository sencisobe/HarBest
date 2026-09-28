// domain/exception/DuplicateHabitNameException.java
package com.sencisobe.harbest.domain.exception;

public class DuplicateHabitNameException extends RuntimeException {

    public DuplicateHabitNameException(String name) {
        super("A habit named '" + name + "' already exists");
    }
}