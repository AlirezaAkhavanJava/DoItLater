package com.arcade.doitlater.Habit.Service;

import com.arcade.doitlater.Habit.Domain.dto.request.CreateHabitRequest;
import com.arcade.doitlater.Habit.Domain.dto.request.ToggleHabitEntryRequest;
import com.arcade.doitlater.Habit.Domain.dto.response.HabitDto;
import com.arcade.doitlater.Habit.Domain.dto.response.WeekGridDto;
import com.arcade.doitlater.Habit.Domain.entity.Habit;
import com.arcade.doitlater.Habit.Domain.entity.HabitEntry;

import java.time.LocalDate;
import java.util.List;

public interface HabitService {
    Habit createNewHabit(CreateHabitRequest request);

    List<HabitDto> findAll();

    WeekGridDto getWeekGrid(LocalDate weekStart);

    HabitDto getHabit(Long id);

    HabitEntry toggleEntry(Long habitId, ToggleHabitEntryRequest request);

    HabitDto updateHabit(Long id, CreateHabitRequest request);

    void deleteHabit(Long id);
}