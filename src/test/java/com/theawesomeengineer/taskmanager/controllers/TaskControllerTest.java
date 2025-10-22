package com.theawesomeengineer.taskmanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theawesomeengineer.taskmanager.exceptions.NotFoundException;
import com.theawesomeengineer.taskmanager.payload.model.Task;
import com.theawesomeengineer.taskmanager.payload.model.TaskRequest;
import com.theawesomeengineer.taskmanager.services.TaskService;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getTasks_shouldReturnListOfTasks() throws Exception {
        Task task1 = new Task(1L, "Test Task 1", "1", false, OffsetDateTime.now(), OffsetDateTime.now());
        Task task2 = new Task(2L, "Test Task 2", "2", false, OffsetDateTime.now(), OffsetDateTime.now());
        List<Task> taskList = List.of(task1, task2);

        given(taskService.getTasks()).willReturn(taskList);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Test Task 1"))
                .andExpect(jsonPath("$[1].title").value("Test Task 2"));
    }

    @Test
    void getTask_shouldReturnOneTask() throws Exception {
        Task task = new Task(1L, "Test Task 1", "1", false, OffsetDateTime.now(), OffsetDateTime.now());
        given(taskService.getTask(1L)).willReturn(task);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Task 1"));
    }

    @Test 
    void getTask_shouldReturnNotFound() throws Exception {
        when(taskService.getTask(1L)) 
            .thenThrow(new NotFoundException("Task not found", "Task with ID 1 not found"));
        mockMvc.perform(get("/tasks/1"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Task not found"))
            .andExpect(jsonPath("$.details").value("Task with ID 1 not found"));
    }

    @Test
    void createTask_shouldCallServiceAndReturnOk() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("New Task");
        request.setDescription("New Desc");
        request.setCompleted(false);

        Task createdTask = new Task(100L, "New Task", "New Desc", false, OffsetDateTime.now(), OffsetDateTime.now());

        given(taskService.createTask(anyString(), anyString(), anyBoolean())).willReturn(createdTask);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.title").value("New Task"));
        
        verify(taskService).createTask(eq("New Task"), eq("New Desc"), eq(false));
    }

    @Test
    void deleteTask_shouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(1L);
    }
}