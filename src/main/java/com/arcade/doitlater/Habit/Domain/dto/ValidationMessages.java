package com.arcade.doitlater.Habit.Domain.dto;

public final class ValidationMessages {
    public static final String NAME_LENGTH_ERROR = "Name must be between 2 and 100 characters";
    public static final String BLANK_NAME_ERROR = "Name can not be blank";
    public static final String BLANK_DESCRIPTION_ERROR = "Description can not be blank";
    public static final String DESCRIPTION_LENGTH_ERROR = "Description must have less than 1000 characters";
    public static final String PRIORITY_NOT_SPECIFIED = "Specify the priority";
    public static final String NULL_MESSAGE = "Specify the property";

    private ValidationMessages() {
    }
}