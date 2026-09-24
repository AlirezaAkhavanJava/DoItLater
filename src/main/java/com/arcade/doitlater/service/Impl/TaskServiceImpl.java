package com.arcade.doitlater.service.Impl;

import com.arcade.doitlater.Repository.TaskRepository;
import com.arcade.doitlater.domain.CreateTaskRequest;
import com.arcade.doitlater.domain.entity.Task;
import com.arcade.doitlater.domain.entity.TaskStatus;
import com.arcade.doitlater.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task createTask(CreateTaskRequest request) {

        //Time comes from the application , the only time user defines is the due date
        Instant now = Instant.now();
        //Create Request (deals with Request DTO)
        Task task = new Task(
                null,
                request.title(),
                request.description(),
                request.dueDate(),
                /*Application side*/
                TaskStatus.OPEN,
                request.priority(),
                now,
                now
        );
        return taskRepository.save(task);
    }
}
