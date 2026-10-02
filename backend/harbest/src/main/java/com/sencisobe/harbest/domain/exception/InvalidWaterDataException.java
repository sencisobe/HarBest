// domain/exception/InvalidWaterDataException.java
package com.sencisobe.harbest.domain.exception;

public class InvalidWaterDataException extends RuntimeException {

    public InvalidWaterDataException(String message) {
        super(message);
    }
}