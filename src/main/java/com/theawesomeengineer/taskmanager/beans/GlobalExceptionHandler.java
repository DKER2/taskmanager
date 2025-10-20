package com.theawesomeengineer.taskmanager.beans;

import java.time.OffsetDateTime;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.theawesomeengineer.taskmanager.exceptions.BaseException;



@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<String> handleDuplicateException(BaseException ex) {
        Map<String, Object> errorBody = Map.of(
            "message", ex.getMessage(),
            "timestamp", OffsetDateTime.now(),
            "details", ex.getStackTrace()
        );
        return ResponseEntity.status(ex.getCode()).body(errorBody.toString());
    }
}
