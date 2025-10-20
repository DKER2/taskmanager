package com.theawesomeengineer.taskmanager.payload.requests;

import lombok.Data;

@Data
public class UpdateTaskRequest {
    private String title;
    private String description;
    private Boolean completed;
}
