package com.arcade.doitlater.domain.dto;


import com.arcade.doitlater.domain.entity.TaskPriority;
import com.arcade.doitlater.domain.entity.TaskStatus;

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
