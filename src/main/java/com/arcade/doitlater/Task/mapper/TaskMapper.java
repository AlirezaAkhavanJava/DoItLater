package com.arcade.doitlater.Task.mapper;

import com.arcade.doitlater.Task.domain.CreateTaskRequest;
import com.arcade.doitlater.Task.domain.UpdateTaskRequest;
import com.arcade.doitlater.Task.domain.dto.CreateTaskRequestDto;
import com.arcade.doitlater.Task.domain.dto.TaskDto;
import com.arcade.doitlater.Task.domain.dto.UpdateTaskRequestDto;
import com.arcade.doitlater.Task.domain.entity.Task;

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


    UpdateTaskRequest fromDto(UpdateTaskRequestDto requestDto);
}
