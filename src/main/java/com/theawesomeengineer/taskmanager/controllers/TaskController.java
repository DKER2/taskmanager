package com.theawesomeengineer.taskmanager.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.theawesomeengineer.taskmanager.payload.requests.CreateTaskRequest;
import com.theawesomeengineer.taskmanager.services.TaskService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/")
    public void createTask(@RequestBody CreateTaskRequest createTaskRequest) {
        taskService.createTask(createTaskRequest.getTitle(), createTaskRequest.getDescription(), createTaskRequest.getCompleted());
    }
}
