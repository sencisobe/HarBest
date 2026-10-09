// infrastructure/web/GlobalExceptionHandler.java
package com.sencisobe.harbest.infrastructure.web;

import com.sencisobe.harbest.domain.exception.DuplicateHabitNameException;
import com.sencisobe.harbest.domain.exception.EmailAlreadyUsedException;
import com.sencisobe.harbest.domain.exception.HabitNotFoundException;
import com.sencisobe.harbest.domain.exception.InvalidCredentialsException;
import com.sencisobe.harbest.domain.exception.InvalidHabitDataException;
import com.sencisobe.harbest.domain.exception.InvalidWaterDataException;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HabitNotFoundException.class)
    public ProblemDetail handleHabitNotFound(HabitNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    @ExceptionHandler(DuplicateHabitNameException.class)
    public ProblemDetail handleDuplicateName(DuplicateHabitNameException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }
    @ExceptionHandler(InvalidHabitDataException.class)
    public ProblemDetail handleInvalidData(InvalidHabitDataException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    @ExceptionHandler(InvalidWaterDataException.class)
    public ProblemDetail handleInvalidWaterData(InvalidWaterDataException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    @ExceptionHandler (EmailAlreadyUsedException.class)
    public ProblemDetail handleEmailAlreadyUsed(EmailAlreadyUsedException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }
    @ExceptionHandler (InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentials(InvalidCredentialsException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        String sMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .findFirst()
                .orElse(" Invalid data");
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, sMessage);
    }
}