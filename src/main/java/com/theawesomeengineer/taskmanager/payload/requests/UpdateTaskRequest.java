package com.theawesomeengineer.taskmanager.payload.requests;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UpdateTaskRequest {
    @NotBlank(message = "Title is required.")
    private String title;
    @NotBlank(message = "Description is required.")
    private String description;
    private Boolean completed;
}
