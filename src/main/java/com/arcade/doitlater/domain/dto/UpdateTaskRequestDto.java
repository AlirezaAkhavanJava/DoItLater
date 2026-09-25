package com.arcade.doitlater.domain.dto;


import com.arcade.doitlater.domain.entity.TaskPriority;
import com.arcade.doitlater.domain.entity.TaskStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

//A Request DTO (as the name mentions)
public record UpdateTaskRequestDto(
        @NotBlank(message = ERROR_MESSAGE_FOR_TITLE)
        @Length(min = 2, max = 255, message = ERROR_MESSAGE_FOR_TITLE)
        String title,

        @Length(max = 1050, message = ERROR_MESSAGE_FOR_DESCRIPTION)
        @Nullable
        String description,

        @Nullable
        @FutureOrPresent(message = ERROR_MESSAGE_FOR_DUE_DATE)
        LocalDate dueDate,

        @NotNull(message = ERROR_MESSAGE_FOR_PRIORITY)
        TaskPriority priority,

        @NotNull(message = ERROR_MESSAGE_FOR_STATUS)
        TaskStatus status
) {
    private static final String ERROR_MESSAGE_FOR_TITLE
            = "Title must be between 2 to 255 characters";
    private static final String ERROR_MESSAGE_FOR_DESCRIPTION
            = "Description must be less than 1050 characters";
    private static final String ERROR_MESSAGE_FOR_DUE_DATE =
            "Due date can not be in the past";
    private static final String ERROR_MESSAGE_FOR_PRIORITY =
            "Priority must be provided";
    private static final String ERROR_MESSAGE_FOR_STATUS =
            "Status must be provided";
}
