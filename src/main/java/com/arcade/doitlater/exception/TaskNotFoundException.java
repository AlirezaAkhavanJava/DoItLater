package com.arcade.doitlater.exception;

import java.util.UUID;


public class TaskNotFoundException extends RuntimeException {

    private final UUID id;

    public TaskNotFoundException(UUID id) {
        super(String.format("The task by ID '%s' not exist.", id));
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
