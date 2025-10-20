package com.theawesomeengineer.taskmanager.beans;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.theawesomeengineer.taskmanager.exceptions.BaseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handleDuplicateException(BaseException ex) {
        String[] stackTrace = Arrays.stream(ex.getStackTrace())
            .map(StackTraceElement::toString)
            .toArray(String[]::new);

        Map<String, Object> errorBody = Map.of(
            "message", ex.getMessage(),
            "timestamp", Instant.now(),
            "details", stackTrace 
        );
        return ResponseEntity
            .status(ex.getCode())
            .body(errorBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
        MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
