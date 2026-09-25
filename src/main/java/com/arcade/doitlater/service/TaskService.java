package com.arcade.doitlater.service;

import com.arcade.doitlater.domain.CreateTaskRequest;
import com.arcade.doitlater.domain.UpdateTaskRequest;
import com.arcade.doitlater.domain.entity.Task;

import java.util.List;
import java.util.UUID;


public interface TaskService {
    Task createTask(CreateTaskRequest request);

    List<Task> listTasks();

    Task updateTask(UUID taskId, UpdateTaskRequest request);

}
