package com.arcade.doitlater.Habit.Domain.dto.response;

import java.time.LocalDate;
import java.util.List;

public record WeekGridDto(
        LocalDate weekStart,
        LocalDate weekEnd,
        String dateRangeDisplay,
        List<LocalDate> days,
        List<HabitDto> habits
) {
}