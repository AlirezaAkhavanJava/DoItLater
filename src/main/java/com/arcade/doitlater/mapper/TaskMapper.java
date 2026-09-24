package com.arcade.doitlater.mapper;

import com.arcade.doitlater.domain.CreateTaskRequest;
import com.arcade.doitlater.domain.dto.CreateTaskRequestDto;
import com.arcade.doitlater.domain.dto.TaskDto;
import com.arcade.doitlater.domain.entity.Task;

public interface TaskMapper {

    /**
     * Takes a {@link CreateTaskRequestDto} and maps it into a {@link CreateTaskRequest}
     * Service Layer TaskService requires a CreateTaskRequest
     * This will save a new task by getting user information and create a {@link CreateTaskRequest}
     */
    CreateTaskRequest fromDto(CreateTaskRequestDto dto);

    /**
     * Takes a {@link TaskDto} and maps it into a {@link Task}
     * This will be sent to the client as the response of the Request
     */
    TaskDto toDto(Task task);
}
