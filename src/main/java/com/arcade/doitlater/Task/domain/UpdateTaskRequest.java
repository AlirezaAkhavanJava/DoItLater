package com.arcade.doitlater.Task.domain;


import com.arcade.doitlater.Task.domain.entity.TaskPriority;
import com.arcade.doitlater.Task.domain.entity.TaskStatus;

import java.time.LocalDate;

public record UpdateTaskRequest(
        String title,
        String description,
        LocalDate dueDate,
        TaskStatus status,
        TaskPriority priority
) {
}
