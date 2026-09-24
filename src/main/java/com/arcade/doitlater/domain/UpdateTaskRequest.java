package com.arcade.doitlater.domain;


import com.arcade.doitlater.domain.entity.TaskPriority;
import com.arcade.doitlater.domain.entity.TaskStatus;

import java.time.LocalDate;

public record UpdateTaskRequest(
        String title,
        String description,
        LocalDate dueDate,
        TaskStatus status,
        TaskPriority priority
) {
}
