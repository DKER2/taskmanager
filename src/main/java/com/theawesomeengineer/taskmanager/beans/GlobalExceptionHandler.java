package com.theawesomeengineer.taskmanager.beans;

import java.time.OffsetDateTime;
import java.util.StringJoiner;

import com.theawesomeengineer.taskmanager.payload.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.theawesomeengineer.taskmanager.exceptions.BaseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Error> handleDuplicateException(BaseException ex) {
        Error formatedError = new Error(
            ex.getMessage(),
            OffsetDateTime.now()
        );

        formatedError.details(ex.getDetailMessage());
        
        return new ResponseEntity<Error>(formatedError, ex.getCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleValidationExceptions(
        MethodArgumentNotValidException ex) {
        
        StringJoiner errorMessage = new StringJoiner(" | ");
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errorMessage.add(error.getDefaultMessage());
        });

        Error formattError = new Error(
            errorMessage.toString(),
            OffsetDateTime.now()
        );
        
        return new ResponseEntity<Error>(formattError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Error> handleInternalServerExceptions(RuntimeException exception) {
        Error formatedError = new Error(
            "Internal server error ",
            OffsetDateTime.now()
        );
        return new ResponseEntity<Error>(formatedError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
