package com.arcade.doitlater.Habit.Domain.dto;

import com.arcade.doitlater.Habit.Domain.entity.HabitPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateHabitRequestDto(

        @NotBlank(message = ValidationMessages.BLANK_NAME_ERROR)
        @Length(min = 2, max = 100, message = ValidationMessages.NAME_LENGTH_ERROR)
        String name,

        @Length(max = 1000, message = ValidationMessages.DESCRIPTION_LENGTH_ERROR)
        String description,

        @NotNull(message = ValidationMessages.PRIORITY_NOT_SPECIFIED)
        HabitPriority priority
) {

}
