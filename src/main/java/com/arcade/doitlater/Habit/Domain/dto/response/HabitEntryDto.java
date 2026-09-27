package com.arcade.doitlater.Habit.Domain.dto.response;

import java.time.LocalDate;

public record HabitEntryDto(
        Long id,
        LocalDate entryDate,
        Boolean completed,
        String note
) {
}