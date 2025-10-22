package com.theawesomeengineer.taskmanager.services;

import com.theawesomeengineer.taskmanager.entities.TaskEntity;
import com.theawesomeengineer.taskmanager.exceptions.NotFoundException;
import com.theawesomeengineer.taskmanager.mappers.TaskMapper;
import com.theawesomeengineer.taskmanager.payload.model.Task;
import com.theawesomeengineer.taskmanager.repositories.TaskRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskMapper taskMapper;

    @Test 
    void getTasks_Success() {
        TaskEntity mockTask1 = TaskEntity.builder().id(1L).title("Task 1").build();
        TaskEntity mockTask2 = TaskEntity.builder().id(2L).title("Task 2").build();

        Task mockTaskResponse1 = new Task(1L, "Task 1", null, null, null, null);
        Task mockTaskResponse2 = new Task(2L, "Task 2", null, null, null, null);

        when(taskRepository.findAll()).thenReturn(Arrays.asList(mockTask1, mockTask2));
        when(taskMapper.toResponseModel(mockTask1)).thenReturn(mockTaskResponse1);
        when(taskMapper.toResponseModel(mockTask2)).thenReturn(mockTaskResponse2);

        List<Task> tasks = taskService.getTasks();
        
        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());
        assertEquals("Task 2", tasks.get(1).getTitle());

        verify(taskRepository, times(1)).findAll();
        verify(taskMapper, times(2)).toResponseModel(any(TaskEntity.class));
    }

    @Test
    void testGetTaskById_Success() {
        TaskEntity mockTask = TaskEntity.builder()
            .id(1L)
            .title("Test Task")
            .build();

        Task mockTaskResponse = new Task(1L, "Test Task", null, null, null, null);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(mockTask));
        when(taskMapper.toResponseModel(mockTask)).thenReturn(mockTaskResponse);

        Task foundTask = taskService.getTask(1L);

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

        assertEquals("Task with ID 99 not found", exception.getDetailMessage());
    }

    @Test
    void testCreateTask() {
        taskService.createTask("Test Title", "Test Description", false);
        verify(taskRepository, times(1)).save(any(TaskEntity.class));
    }

    @Test
    void testDelete_Success() {
        TaskEntity mockTask1 = TaskEntity.builder().id(1L).title("Task 1").build();

        when(taskRepository.findById(1L))
            .thenReturn(Optional.of(mockTask1));
        
        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFound() {
        when(taskRepository.findById(99L))
            .thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(
            NotFoundException.class,
            () -> taskService.deleteTask(99L)
        );
        
        assertEquals(
            "Task not found", 
            thrown.getMessage()
        );

        assertEquals(
            "Task with ID 99 not found", 
            thrown.getDetailMessage()
        );

        verify(taskRepository, never()).deleteById(anyLong());
        verify(taskRepository, times(1)).findById(99L);
    }

    @Test
    void updateTask_Success() {
        TaskEntity existingTask = TaskEntity.builder().id(1L).title("Old Title").description("Old Description").build();

        when(taskRepository.findById(1L))
            .thenReturn(Optional.of(existingTask));

        Task expectedResponse = new Task(1L, "New Title", "New Description", true, null, null);
        
        when(taskMapper.toResponseModel(any(TaskEntity.class))).thenReturn(expectedResponse);
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(existingTask); 

        Task result = taskService.updateTask(
            1L,
            "New Title",
            "New Description", 
            true              
        );

        ArgumentCaptor<TaskEntity> taskCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(taskRepository).save(taskCaptor.capture());
        
        TaskEntity capturedEntity = taskCaptor.getValue();
        assertEquals("New Title", capturedEntity.getTitle());
        assertEquals("New Description", capturedEntity.getDescription());
        assertEquals(true, capturedEntity.getCompleted());

        verify(taskRepository, times(1)).findById(1L);
        verify(taskMapper, times(1)).toResponseModel(existingTask);

        assertEquals(expectedResponse.getTitle(), result.getTitle());
        assertEquals(expectedResponse.getCompleted(), result.getCompleted());
    }

    @Test
    void updateTask_NotFound() {
        when(taskRepository.findById(1L))
            .thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(
            NotFoundException.class,
            () -> taskService.updateTask(1L, "T", "D", false)
        );

        assertEquals(
            "Task not found", 
            thrown.getMessage()
        );

        assertEquals(
            "Task with ID 1 not found", 
            thrown.getDetailMessage()
        );

        verify(taskRepository, never()).save(any());
        verify(taskMapper, never()).toResponseModel(any());
    }
}