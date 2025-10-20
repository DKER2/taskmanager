package com.theawesomeengineer.taskmanager.payload.requests;

import lombok.Data;

@Data
public class CreateTaskRequest {
    String title;
    String description;
    Boolean completed = false;
}
