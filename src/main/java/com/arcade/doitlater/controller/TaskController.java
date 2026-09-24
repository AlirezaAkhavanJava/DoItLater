package com.arcade.doitlater.controller;

import com.arcade.doitlater.domain.CreateTaskRequest;
import com.arcade.doitlater.domain.dto.CreateTaskRequestDto;
import com.arcade.doitlater.domain.dto.TaskDto;
import com.arcade.doitlater.domain.entity.Task;
import com.arcade.doitlater.mapper.TaskMapper;
import com.arcade.doitlater.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping(path = "/createTask")
    public ResponseEntity<TaskDto> createTask(
            @Valid @RequestBody CreateTaskRequestDto createTaskRequestDto
    ) {
        //Used in service layer - we are converting a createTaskRequestDto(From client) to CreateTaskRequest(to Service)
        CreateTaskRequest createdTaskRequest = taskMapper.fromDto(createTaskRequestDto);

        //calling and creating a createTaskRequest - Passing the created task request into the service layer
        Task task = taskService.createTask(createdTaskRequest);

        //to show a response to user
        TaskDto createdTaskDto = taskMapper.toDto(task);
        return new ResponseEntity<>(createdTaskDto, HttpStatus.CREATED);

    }


    @GetMapping()
    public ResponseEntity<List<TaskDto>> listTasks() {
        List<Task> tasks = taskService.listTasks();
        List<TaskDto> taskDtoList = tasks.stream().map(taskMapper::toDto).toList();
        return ResponseEntity.ok(taskDtoList);

    }

}
