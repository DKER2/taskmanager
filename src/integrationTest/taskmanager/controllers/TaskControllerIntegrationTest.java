package com.theawesomeengineer.taskmanager.controllers;

import com.theawesomeengineer.taskmanager.payload.model.Task;
import com.theawesomeengineer.taskmanager.repositories.TaskRepository;
import com.theawesomeengineer.taskmanager.entities.TaskEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Full integration test.
 * 1. Starts the ENTIRE Spring Boot application.
 * 2. Starts a REAL MySQL database in a Docker container (using Testcontainers).
 * 3. Connects the app to the database.
 * 4. Sends REAL HTTP requests.
 */
@Testcontainers 
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

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

        assertNotNull(postResponse.getBody());
        Long newTaskId = postResponse.getBody().getId();
        
        ResponseEntity<Task> getResponse = restTemplate.getForEntity(
                "/tasks/" + newTaskId,
                Task.class
        );

        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
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

        ResponseEntity<Task> getResponse = restTemplate.getForEntity(
                "/tasks/" + taskId,
                Task.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals("Updated Title", getResponse.getBody().getTitle());
        assertEquals("Updated Desc", getResponse.getBody().getDescription());
        assertEquals(true, getResponse.getBody().getCompleted());
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

