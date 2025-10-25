package com.theawesomeengineer.taskmanager.controllers;

import com.theawesomeengineer.taskmanager.payload.model.Task;
import com.theawesomeengineer.taskmanager.repositories.TaskRepository;
import com.theawesomeengineer.taskmanager.entities.TaskEntity;
import com.theawesomeengineer.taskmanager.IntegrationTestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers 
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerIntegrationTest extends IntegrationTestBase {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskRepository taskRepository;

    @AfterEach
    void tearDown() {
        taskRepository.deleteAll();
    }

    private TaskEntity createTestTask(String title, String description) {
        TaskEntity task = new TaskEntity();
        task.setTitle(title);
        task.setDescription(description);
        task.setCompleted(false);
        return taskRepository.save(task);
    }

    @Test
    void shouldCreateAndRetrieveTask() {
        Task taskToCreate = new Task();
        taskToCreate.setTitle("Test Task");
        taskToCreate.setDescription("Create task test");
        taskToCreate.setCompleted(false);

        ResponseEntity<Task> postResponse = restTemplate.postForEntity(
                "/tasks", 
                taskToCreate,
                Task.class
        );

        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        assertNotNull(postResponse.getBody());
        Long newTaskId = postResponse.getBody().getId();
        
        ResponseEntity<Task> getResponse = restTemplate.getForEntity(
                "/tasks/" + newTaskId,
                Task.class
        );
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody()); 
        assertEquals("Test Task", getResponse.getBody().getTitle());
        assertEquals("Create task test", getResponse.getBody().getDescription());
    }

    @Test
    void shouldReturnAllTasks() {
        createTestTask("Task 1", "Desc 1");
        createTestTask("Task 2", "Desc 2");

        ResponseEntity<Task[]> response = restTemplate.getForEntity(
                "/tasks",
                Task[].class 
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
        assertEquals("Task 1", response.getBody()[0].getTitle());
        assertEquals("Task 2", response.getBody()[1].getTitle());
    }

    @Test
    void shouldUpdateTask() {
        TaskEntity existingTask = createTestTask("Original Title", "Original Desc");
        Long taskId = existingTask.getId();

        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Title");
        updatedTask.setDescription("Updated Desc");
        updatedTask.setCompleted(true);

        ResponseEntity<Task> putResponse = restTemplate.exchange(
                "/tasks/" + taskId,
                HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(updatedTask),
                Task.class
        );

        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        assertNotNull(putResponse.getBody());
        assertEquals("Updated Title", putResponse.getBody().getTitle());
        assertEquals("Updated Desc", putResponse.getBody().getDescription());
        assertEquals(true, putResponse.getBody().getCompleted());
    }

    @Test
    void shouldDeleteTask() {
        TaskEntity existingTask = createTestTask("To Be Deleted", "Delete me");
        Long taskId = existingTask.getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/tasks/" + taskId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<Task> getResponse = restTemplate.getForEntity(
                "/tasks/" + taskId,
                Task.class
        );

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void shouldReturnNotFoundForInvalidId() {
        Long nonExistentId = 9999L;

        ResponseEntity<Task> getResponse = restTemplate.getForEntity(
                "/tasks/" + nonExistentId,
                Task.class
        );

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}

