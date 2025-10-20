package com.theawesomeengineer.taskmanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theawesomeengineer.taskmanager.entities.TaskEntity;
import com.theawesomeengineer.taskmanager.payload.requests.CreateTaskRequest;
import com.theawesomeengineer.taskmanager.services.TaskService;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = TaskController.class,
    excludeAutoConfiguration = { 
        HibernateJpaAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class
    }
)
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
        TaskEntity task1 = TaskEntity.builder()
                .id(1L)
                .title("Test Task 1")
                .description("Desc 1")
                .build();
        TaskEntity task2 = TaskEntity.builder()
                .id(2L) 
                .title("Test Task 2")
                .description("Desc 2")
                .build();
        List<TaskEntity> taskList = List.of(task1, task2);

        given(taskService.getTasks()).willReturn(taskList);

        mockMvc.perform(get("/api/tasks/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Test Task 1"))
                .andExpect(jsonPath("$[1].title").value("Test Task 2"));
    }

    @Test
    void getMethodName_shouldReturnOneTask() throws Exception {
        TaskEntity task = TaskEntity.builder()
                .id(1L)
                .title("Test Task 1")
                .description("Desc 1")
                .build();
        given(taskService.getTask(1L)).willReturn(task);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Task 1"));
    }

    @Test
    void createTask_shouldCallServiceAndReturnOk() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("New Task");
        request.setDescription("New Desc");
        request.setCompleted(false);

        mockMvc.perform(post("/api/tasks/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()); 

        verify(taskService).createTask("New Task", "New Desc", false);
    }

    @Test
    void deleteTask_shouldCallServiceAndReturnOk() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk());

        verify(taskService).deleteTask(1L);
    }
}