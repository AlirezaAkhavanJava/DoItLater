package com.arcade.doitlater.Habit.Domain.dto.request;

import com.arcade.doitlater.Habit.Domain.entity.HabitPriority;

public record CreateHabitRequest(
        String name,
        String description,
        HabitPriority priority
) {}