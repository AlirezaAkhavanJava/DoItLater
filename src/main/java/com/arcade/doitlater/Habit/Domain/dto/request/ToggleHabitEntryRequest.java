package com.arcade.doitlater.Habit.Domain.dto.request;

import java.time.LocalDate;

public record ToggleHabitEntryRequest(
        LocalDate entryDate,
        Boolean completed,
        String note
) {
}