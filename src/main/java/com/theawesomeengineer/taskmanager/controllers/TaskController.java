package com.theawesomeengineer.taskmanager.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.theawesomeengineer.taskmanager.api.TasksApi;
import com.theawesomeengineer.taskmanager.entities.TaskEntity;
import com.theawesomeengineer.taskmanager.payload.requests.CreateTaskRequest;
import com.theawesomeengineer.taskmanager.payload.requests.UpdateTaskRequest;
import com.theawesomeengineer.taskmanager.services.TaskService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController implements TasksApi {
    private final TaskService taskService;

    @PostMapping("")
    public ResponseEntity<TaskEntity> createTask(@Valid @RequestBody CreateTaskRequest createTaskRequest) {
        TaskEntity createdTask = taskService.createTask(createTaskRequest.getTitle(), createTaskRequest.getDescription(), createTaskRequest.getCompleted());
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequestUri()
            .path("/{id}")
            .buildAndExpand(createdTask.getId())
            .toUri();
        return ResponseEntity.created(location).body(createdTask);
    }

    @GetMapping("")
    public List<TaskEntity> getTasks() {
        return taskService.getTasks();
    }

    @GetMapping("/{id}")
    public TaskEntity getMethodName(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    @PutMapping("/{id}")
    public TaskEntity updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest updateTaskRequest) {
        return taskService.updateTask(id, updateTaskRequest.getTitle(), updateTaskRequest.getDescription(), updateTaskRequest.getCompleted());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
