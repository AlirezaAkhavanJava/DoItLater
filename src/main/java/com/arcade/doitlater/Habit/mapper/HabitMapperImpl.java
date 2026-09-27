package com.arcade.doitlater.Habit.mapper;

import com.arcade.doitlater.Habit.Domain.dto.CreateHabitRequestDto;
import com.arcade.doitlater.Habit.Domain.dto.ToggleHabitEntryRequestDto;
import com.arcade.doitlater.Habit.Domain.dto.request.CreateHabitRequest;
import com.arcade.doitlater.Habit.Domain.dto.request.ToggleHabitEntryRequest;
import com.arcade.doitlater.Habit.Domain.dto.response.HabitDto;
import com.arcade.doitlater.Habit.Domain.dto.response.HabitEntryDto;
import com.arcade.doitlater.Habit.Domain.entity.Habit;
import com.arcade.doitlater.Habit.Domain.entity.HabitEntry;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HabitMapperImpl implements HabitMapper {

    @Override
    public CreateHabitRequest fromDto(CreateHabitRequestDto requestDto) {
        return new CreateHabitRequest(
                requestDto.name(),
                requestDto.description(),
                requestDto.priority()
        );
    }

    @Override
    public ToggleHabitEntryRequest fromDto(ToggleHabitEntryRequestDto requestDto) {
        return new ToggleHabitEntryRequest(
                requestDto.entryDate(),
                requestDto.completed(),
                requestDto.note()
        );
    }

    @Override
    public HabitEntryDto toDto(HabitEntry habitEntry) {
        return new HabitEntryDto(
                habitEntry.getId(),
                habitEntry.getEntryDate(),
                habitEntry.getCompleted(),
                habitEntry.getNote()
        );
    }

    @Override
    public HabitDto toDto(Habit habit, List<HabitEntry> entriesForRange, int totalDays) {
        List<HabitEntryDto> entryDtos = entriesForRange.stream()
                .map(this::toDto)
                .toList();

        int completed = (int) entriesForRange.stream()
                .filter(HabitEntry::getCompleted)
                .count();
        double rate = totalDays == 0 ? 0.0 : (completed * 100.0) / totalDays;

        return new HabitDto(
                habit.getId(),
                habit.getName(),
                habit.getDescription(),
                habit.getPriority(),
                habit.getCreatedDate(),
                entryDtos,
                completed,
                totalDays,
                rate
        );
    }
}