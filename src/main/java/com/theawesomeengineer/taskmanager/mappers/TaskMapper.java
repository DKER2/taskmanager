package com.theawesomeengineer.taskmanager.mappers;

import org.mapstruct.Mapper;
import com.theawesomeengineer.taskmanager.entities.TaskEntity;
import com.theawesomeengineer.taskmanager.payload.model.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toResponseModel(TaskEntity taskEntity);

    TaskEntity toEntity(Task task);
}
