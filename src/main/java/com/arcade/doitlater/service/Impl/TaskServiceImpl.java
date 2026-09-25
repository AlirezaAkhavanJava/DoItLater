package com.arcade.doitlater.service.Impl;

import com.arcade.doitlater.Repository.TaskRepository;
import com.arcade.doitlater.domain.CreateTaskRequest;
import com.arcade.doitlater.domain.UpdateTaskRequest;
import com.arcade.doitlater.domain.entity.Task;
import com.arcade.doitlater.domain.entity.TaskStatus;
import com.arcade.doitlater.exception.TaskNotFoundException;
import com.arcade.doitlater.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

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

    @Override
    public List<Task> listTasks() {
        return taskRepository.findAll(Sort.by(Sort.Direction.ASC, "created"));
    }

    @Override
    public Task updateTask(UUID taskId, UpdateTaskRequest request) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        task.setStatus(request.status());
        task.setPriority(request.priority());
        task.setUpdated(Instant.now());

        return taskRepository.save(task);
    }

    @Override
    public void removeTask(UUID id) {
        taskRepository.deleteById(id);
    }
}
