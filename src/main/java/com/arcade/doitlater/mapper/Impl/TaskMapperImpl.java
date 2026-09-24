package com.arcade.doitlater.mapper.Impl;

import com.arcade.doitlater.domain.CreateTaskRequest;
import com.arcade.doitlater.domain.dto.CreateTaskRequestDto;
import com.arcade.doitlater.domain.dto.TaskDto;
import com.arcade.doitlater.domain.entity.Task;
import com.arcade.doitlater.mapper.TaskMapper;
import com.arcade.doitlater.service.Impl.TaskServiceImpl;
import org.springframework.stereotype.Component;


@Component
public class TaskMapperImpl implements TaskMapper {
    /**
     * creates a {@link CreateTaskRequest} From a {@link CreateTaskRequestDto} that is taken from the {@code user}
     * {@link CreateTaskRequest} is used in the {@link TaskServiceImpl} to create a {@link Task}
     */
    @Override
    public CreateTaskRequest fromDto(CreateTaskRequestDto dto) {
        return new CreateTaskRequest(
                dto.title(),
                dto.description(),
                dto.dueDate(),
                dto.priority()
        );
    }

    /**
     * Gets a {@link Task} Entity From the {@code Repository} layer
     * and Returns the created {@link TaskDto} to the {@code client}
     */
    @Override
    public TaskDto toDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getPriority(),
                task.getStatus()
        );
    }
}
