package com.arcade.doitlater.Habit.mapper;

import com.arcade.doitlater.Habit.Domain.dto.CreateHabitRequestDto;
import com.arcade.doitlater.Habit.Domain.dto.ToggleHabitEntryRequestDto;
import com.arcade.doitlater.Habit.Domain.dto.request.CreateHabitRequest;
import com.arcade.doitlater.Habit.Domain.dto.request.ToggleHabitEntryRequest;
import com.arcade.doitlater.Habit.Domain.dto.response.HabitDto;
import com.arcade.doitlater.Habit.Domain.dto.response.HabitEntryDto;
import com.arcade.doitlater.Habit.Domain.entity.Habit;
import com.arcade.doitlater.Habit.Domain.entity.HabitEntry;

import java.util.List;

public interface HabitMapper {
    CreateHabitRequest fromDto(CreateHabitRequestDto requestDto);

    ToggleHabitEntryRequest fromDto(ToggleHabitEntryRequestDto requestDto);

    HabitEntryDto toDto(HabitEntry habitEntry);

    HabitDto toDto(Habit habit, List<HabitEntry> entriesForRange, int totalDays);
}