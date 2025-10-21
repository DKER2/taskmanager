package com.theawesomeengineer.taskmanager.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TaskEntity {
    @Id
    @GeneratedValue
    Long id;

    @Column
    String title; 

    @Column
    String description;

    @Column
    Boolean completed;

    @CreatedDate
    protected LocalDateTime createdAt;

    @LastModifiedDate
    protected LocalDateTime updatedAt;
}
