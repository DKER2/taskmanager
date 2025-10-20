package com.theawesomeengineer.taskmanager.services;

import com.theawesomeengineer.taskmanager.entities.TaskEntity;
import com.theawesomeengineer.taskmanager.exceptions.NotFoundException;
import com.theawesomeengineer.taskmanager.repositories.TaskRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void testGetTaskById_Success() {
        TaskEntity mockTask = TaskEntity.builder()
            .id(1L)
            .title("Test Task")
            .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(mockTask));

        TaskEntity foundTask = taskService.getTask(1L);

        assertNotNull(foundTask);
        assertEquals(1L, foundTask.getId());
        assertEquals("Test Task", foundTask.getTitle());

        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTaskById_NotFound() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            taskService.getTask(99L);
        });

        assertEquals("Task with ID 99 not found", exception.getMessage());
    }

    @Test
    void testCreateTask() {
        taskService.createTask("Test Title", "Test Description", false);
        verify(taskRepository, times(1)).save(any(TaskEntity.class));
    }
}