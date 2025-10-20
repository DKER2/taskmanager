package com.theawesomeengineer.taskmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.theawesomeengineer.taskmanager.entities.TaskEntity;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}