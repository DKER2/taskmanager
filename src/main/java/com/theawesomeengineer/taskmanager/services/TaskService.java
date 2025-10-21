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

    public TaskEntity createTask(String title, String description, boolean completed) {
        TaskEntity newTask = TaskEntity.builder()
            .title(title)
            .description(description)
            .completed(completed)
            .build();
        taskRepository.save(newTask);
        return newTask;
    }

    public TaskEntity getTask(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found", String.format("Task with ID %d not found", id)));
    }

    public List<TaskEntity> getTasks() {
        return taskRepository.findAll();
    }

    public void deleteTask(Long id) {
        taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found", String.format("Task with ID %d not found", id)));
        taskRepository.deleteById(id);
    }

    public TaskEntity updateTask(Long id, String title, String description, Boolean completed) {
        TaskEntity task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found", String.format("Task with ID %d not found", id)));

        task.setTitle(title);
        task.setDescription(description);
        task.setCompleted(completed);

        return taskRepository.save(task);
    }
}
