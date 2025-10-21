package com.theawesomeengineer.taskmanager.services;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.theawesomeengineer.taskmanager.entities.TaskEntity;
import com.theawesomeengineer.taskmanager.exceptions.NotFoundException;
import com.theawesomeengineer.taskmanager.mappers.TaskMapper;
import com.theawesomeengineer.taskmanager.payload.model.Task;
import com.theawesomeengineer.taskmanager.repositories.TaskRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;


    public Task createTask(String title, String description, boolean completed) {
        TaskEntity newTask = TaskEntity.builder()
            .title(title)
            .description(description)
            .completed(completed)
            .build();
        taskRepository.save(newTask);
        return taskMapper.toResponseModel(newTask);
    }

    public Task getTask(Long id) {
        return taskRepository.findById(id).map(taskMapper::toResponseModel).orElseThrow(() -> new NotFoundException("Task not found", String.format("Task with ID %d not found", id)));
    }

    public List<Task> getTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toResponseModel)
                .collect(Collectors.toList());
    }

    public void deleteTask(Long id) {
        taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found", String.format("Task with ID %d not found", id)));
        taskRepository.deleteById(id);
    }

    public Task updateTask(Long id, String title, String description, Boolean completed) {
        TaskEntity task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found", String.format("Task with ID %d not found", id)));

        task.setTitle(title);
        task.setDescription(description);
        task.setCompleted(completed);

        taskRepository.save(task);
        return taskMapper.toResponseModel(task);
    }
}
