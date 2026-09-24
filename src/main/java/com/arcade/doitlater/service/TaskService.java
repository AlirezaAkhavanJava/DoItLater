package com.arcade.doitlater.service;

import com.arcade.doitlater.domain.CreateTaskRequest;
import com.arcade.doitlater.domain.entity.Task;

import java.util.List;


public interface TaskService {
    Task createTask(CreateTaskRequest request);

    List<Task> listTasks();

}
