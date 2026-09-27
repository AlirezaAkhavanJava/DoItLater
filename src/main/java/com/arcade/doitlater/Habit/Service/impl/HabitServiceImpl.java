package com.arcade.doitlater.Habit.Service.impl;

import com.arcade.doitlater.Habit.Domain.dto.request.CreateHabitRequest;
import com.arcade.doitlater.Habit.Domain.dto.request.ToggleHabitEntryRequest;
import com.arcade.doitlater.Habit.Domain.dto.response.HabitDto;
import com.arcade.doitlater.Habit.Domain.dto.response.WeekGridDto;
import com.arcade.doitlater.Habit.Domain.entity.Habit;
import com.arcade.doitlater.Habit.Domain.entity.HabitEntry;
import com.arcade.doitlater.Habit.Repository.HabitEntryRepository;
import com.arcade.doitlater.Habit.Repository.HabitRepository;
import com.arcade.doitlater.Habit.Service.HabitService;
import com.arcade.doitlater.Habit.mapper.HabitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

    private static final int DAYS_IN_WEEK = 7;

    private final HabitRepository habitRepository;
    private final HabitEntryRepository habitEntryRepository;
    private final HabitMapper habitMapper;

    @Override
    @Transactional(readOnly = true)
    public HabitDto getHabit(Long id) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Habit not found: " + id));

        List<HabitEntry> entries = habitEntryRepository.findByHabitId(habit.getId());
        int total = entries.size();
        if (total == 0) total = 7; // fallback so rate isn't NaN for brand-new habits

        return habitMapper.toDto(habit, entries, total);
    }

    @Override
    @Transactional
    public HabitDto updateHabit(Long id, CreateHabitRequest request) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Habit not found: " + id));

        habit.setName(request.name());
        habit.setDescription(request.description());
        habit.setPriority(request.priority());

        Habit saved = habitRepository.save(habit);
        List<HabitEntry> entries = habitEntryRepository.findByHabitId(saved.getId());
        int total = entries.isEmpty() ? 7 : entries.size();

        return habitMapper.toDto(saved, entries, total);
    }

    @Override
    @Transactional
    public void deleteHabit(Long id) {
        if (!habitRepository.existsById(id)) {
            throw new IllegalArgumentException("Habit not found: " + id);
        }
        habitRepository.deleteById(id); // cascade removes entries
    }


    @Override
    @Transactional
    public Habit createNewHabit(CreateHabitRequest request) {
        Habit habit = new Habit();
        habit.setName(request.name());
        habit.setDescription(request.description());
        habit.setPriority(request.priority());
        habit.setEntries(new ArrayList<>());
        return habitRepository.save(habit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HabitDto> findAll() {
        return habitRepository.findAllByOrderByCreatedDateDesc().stream()
                .map(h -> habitMapper.toDto(h, List.of(), DAYS_IN_WEEK))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public WeekGridDto getWeekGrid(LocalDate weekStart) {
        LocalDate start = weekStart.with(DayOfWeek.MONDAY);
        LocalDate end = start.plusDays(DAYS_IN_WEEK - 1);

        List<Habit> habits = habitRepository.findAllByOrderByCreatedDateDesc();

        List<Long> habitIds = habits.stream().map(Habit::getId).toList();
        List<HabitEntry> allEntries = habitIds.isEmpty()
                ? List.of()
                : habitEntryRepository.findByHabitIdsAndDateRange(habitIds, start, end);

        Map<Long, List<HabitEntry>> entriesByHabit = allEntries.stream()
                .collect(Collectors.groupingBy(e -> e.getHabit().getId()));

        List<HabitDto> habitDtos = habits.stream()
                .map(h -> habitMapper.toDto(
                        h,
                        entriesByHabit.getOrDefault(h.getId(), List.of()),
                        DAYS_IN_WEEK
                ))
                .toList();

        List<LocalDate> days = IntStream.range(0, DAYS_IN_WEEK)
                .mapToObj(start::plusDays)
                .toList();

        return new WeekGridDto(start, end, formatRange(start, end), days, habitDtos);
    }

    @Override
    @Transactional
    public HabitEntry toggleEntry(Long habitId, ToggleHabitEntryRequest request) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new IllegalArgumentException("Habit not found: " + habitId));

        HabitEntry entry = habitEntryRepository
                .findByHabitIdAndEntryDate(habitId, request.entryDate())
                .orElseGet(() -> {
                    HabitEntry e = new HabitEntry();
                    e.setHabit(habit);
                    e.setEntryDate(request.entryDate());
                    e.setCompleted(false);
                    return e;
                });

        entry.setCompleted(request.completed());
        if (request.note() != null) {
            entry.setNote(request.note());
        }

        return habitEntryRepository.save(entry);
    }

    private String formatRange(LocalDate start, LocalDate end) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d");
        return start.format(fmt) + " - " + end.format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
    }



}