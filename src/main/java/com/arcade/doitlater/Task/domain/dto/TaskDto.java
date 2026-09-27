package com.arcade.doitlater.Task.domain.dto;


import com.arcade.doitlater.Task.domain.entity.TaskPriority;
import com.arcade.doitlater.Task.domain.entity.TaskStatus;

import java.time.LocalDate;
import java.util.UUID;

// This is the Response DTO
public record TaskDto(
        UUID id,
        String title,
        String description,
        LocalDate dueDate,
        TaskPriority priority,
        TaskStatus status
) {

}
