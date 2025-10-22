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

import com.theawesomeengineer.taskmanager.payload.api.TasksApi;
import com.theawesomeengineer.taskmanager.payload.model.Task;
import com.theawesomeengineer.taskmanager.payload.model.TaskRequest;
import com.theawesomeengineer.taskmanager.services.TaskService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController implements TasksApi {
    private final TaskService taskService;

    @Override
    @PostMapping("")
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskRequest createTaskRequest) {
        Task createdTask = taskService.createTask(createTaskRequest.getTitle(), createTaskRequest.getDescription(), createTaskRequest.getCompleted());
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequestUri()
            .path("/{id}")
            .buildAndExpand(createdTask.getId())
            .toUri();
        return ResponseEntity.created(location).body(createdTask);
    }

    @Override
    @GetMapping("")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> taskList = taskService.getTasks();

        return ResponseEntity.ok().body(taskList);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTask(id);
        return ResponseEntity.ok().body(task);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest updateTaskRequest) {
        Task updatedTask = taskService.updateTask(id, updateTaskRequest.getTitle(), updateTaskRequest.getDescription(), updateTaskRequest.getCompleted());
        return ResponseEntity.ok().body(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
