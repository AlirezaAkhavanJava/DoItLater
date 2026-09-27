package com.arcade.doitlater.Habit.Domain.dto.response;

import com.arcade.doitlater.Habit.Domain.entity.HabitPriority;

import java.time.LocalDateTime;
import java.util.List;

public record HabitDto(
        Long id,
        String name,
        String description,
        HabitPriority priority,
        LocalDateTime createdDate,
        List<HabitEntryDto> entries,
        int completedCount,
        int totalDays,
        double completionRate
) {
}