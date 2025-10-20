package com.theawesomeengineer.taskmanager.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class TaskController {
    private final TaskService taskService;

    @PostMapping("")
    public TaskEntity createTask(@Valid @RequestBody CreateTaskRequest createTaskRequest) {
        return taskService.createTask(createTaskRequest.getTitle(), createTaskRequest.getDescription(), createTaskRequest.getCompleted());
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
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
