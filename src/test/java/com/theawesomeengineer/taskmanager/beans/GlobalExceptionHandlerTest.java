package com.theawesomeengineer.taskmanager.beans;

import com.theawesomeengineer.taskmanager.exceptions.BaseException;
import com.theawesomeengineer.taskmanager.payload.model.Error;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleDuplicateException_ShouldReturnProperErrorResponse() {
        BaseException baseException = new BaseException(
                "Duplicate task",
                "Task with title already exists",
                HttpStatus.CONFLICT
        );

        ResponseEntity<Error> response = handler.handleDuplicateException(baseException);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Duplicate task", response.getBody().getMessage());
        assertEquals("Task with title already exists", response.getBody().getDetails());
        assertTrue(response.getBody().getTimestamp().isBefore(OffsetDateTime.now().plusSeconds(1)));
    }

    @Test
    void handleValidationExceptions_ShouldAggregateMessages() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(List.of(
                new ObjectError("taskRequest", "Title is required"),
                new ObjectError("taskRequest", "Description cannot be empty")
        ));

        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Error> response = handler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Error body = response.getBody();
        assertNotNull(body);
        assertTrue(body.getMessage().contains("Title is required"));
        assertTrue(body.getMessage().contains("Description cannot be empty"));
        assertNotNull(body.getTimestamp());
    }

    @Test
    void handleInternalServerExceptions_ShouldReturn500WithGenericMessage() {
        RuntimeException ex = new RuntimeException("Something failed internally");

        ResponseEntity<Error> response = handler.handleInternalServerExceptions(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Internal server error", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }
}
