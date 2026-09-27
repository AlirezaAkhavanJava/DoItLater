package com.arcade.doitlater.Habit.Domain.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ToggleHabitEntryRequestDto(
        @NotNull(message = ValidationMessages.NULL_MESSAGE) LocalDate entryDate,
        @NotNull(message = ValidationMessages.NULL_MESSAGE) Boolean completed,
        String note
) {
}