package com.arcade.doitlater.Task.service;

import com.arcade.doitlater.Task.domain.CreateTaskRequest;
import com.arcade.doitlater.Task.domain.UpdateTaskRequest;
import com.arcade.doitlater.Task.domain.entity.Task;

import java.util.List;
import java.util.UUID;


public interface TaskService {
    Task createTask(CreateTaskRequest request);

    List<Task> listTasks();

    Task updateTask(UUID taskId, UpdateTaskRequest request);

    void removeTask(UUID id);
}
