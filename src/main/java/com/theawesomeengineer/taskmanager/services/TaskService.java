package com.theawesomeengineer.taskmanager.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.theawesomeengineer.taskmanager.entities.TaskEntity;
import com.theawesomeengineer.taskmanager.exceptions.NotFoundException;
import com.theawesomeengineer.taskmanager.repositories.TaskRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public void createTask(String title, String description, boolean completed) {
        TaskEntity newTask = TaskEntity.builder()
            .title(title)
            .description(description)
            .completed(completed)
            .build();
        taskRepository.save(newTask);
    }

    public TaskEntity getTask(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Task with ID %d not found", id)));
    }

    public List<TaskEntity> getTasks() {
        return taskRepository.findAll();
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);;
    }
}
