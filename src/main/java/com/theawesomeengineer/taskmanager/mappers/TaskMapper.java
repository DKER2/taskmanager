package com.theawesomeengineer.taskmanager.mappers;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.mapstruct.Mapper;
import com.theawesomeengineer.taskmanager.entities.TaskEntity;
import com.theawesomeengineer.taskmanager.payload.model.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    
    Task toResponseModel(TaskEntity taskEntity);

    TaskEntity toEntity(Task task);

    default Instant map(OffsetDateTime value) {
        if (value == null) {
            return null;
        }
        return value.toInstant();
    }

    default OffsetDateTime map(Instant value) {
        if (value == null) {
            return null;
        }
        return value.atOffset(ZoneOffset.UTC);
    }
}
