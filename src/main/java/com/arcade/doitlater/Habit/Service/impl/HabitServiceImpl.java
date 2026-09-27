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
import java.time.temporal.ChronoUnit;
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

    // ------------------------------------------------------------
    //  Create
    // ------------------------------------------------------------
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

    // ------------------------------------------------------------
    //  Read: all habits
    // ------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<HabitDto> findAll() {
        return habitRepository.findAllByOrderByCreatedDateDesc().stream()
                .map(h -> {
                    List<HabitEntry> entries = habitEntryRepository.findByHabitId(h.getId());
                    int total = computeTotalDays(
                            h,
                            LocalDate.now().minusYears(10),
                            LocalDate.now(),
                            entries
                    );
                    return habitMapper.toDto(h, entries, total);
                })
                .toList();
    }

    // ------------------------------------------------------------
    //  Read: week grid
    // ------------------------------------------------------------
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
                .map(h -> {
                    List<HabitEntry> hEntries =
                            entriesByHabit.getOrDefault(h.getId(), List.of());
                    int total = computeTotalDays(h, start, end, hEntries);
                    return habitMapper.toDto(h, hEntries, total);
                })
                .toList();

        List<LocalDate> days = IntStream.range(0, DAYS_IN_WEEK)
                .mapToObj(start::plusDays)
                .toList();

        return new WeekGridDto(start, end, formatRange(start, end), days, habitDtos);
    }

    // ------------------------------------------------------------
    //  Read: one habit with ALL its entries (for detail page)
    // ------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public HabitDto getHabit(Long id) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Habit not found: " + id));

        List<HabitEntry> entries = habitEntryRepository.findByHabitId(id);
        int total = computeTotalDays(
                habit,
                LocalDate.now().minusYears(10),
                LocalDate.now(),
                entries
        );

        return habitMapper.toDto(habit, entries, total);
    }

    // ------------------------------------------------------------
    //  Write: toggle today's entry (upsert)
    // ------------------------------------------------------------
    @Override
    @Transactional
    public HabitEntry toggleEntry(Long habitId, ToggleHabitEntryRequest request) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new IllegalArgumentException("Habit not found: " + habitId));

        if (!request.entryDate().equals(LocalDate.now())) {
            throw new IllegalArgumentException("Only today's entry can be toggled");
        }

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

    // ------------------------------------------------------------
    //  Write: update
    // ------------------------------------------------------------
    @Override
    @Transactional
    public HabitDto updateHabit(Long id, CreateHabitRequest request) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Habit not found: " + id));

        habit.setName(request.name());
        habit.setDescription(request.description());
        habit.setPriority(request.priority());

        Habit saved = habitRepository.save(habit);

        List<HabitEntry> entries = habitEntryRepository.findByHabitId(id);
        int total = computeTotalDays(
                saved,
                LocalDate.now().minusYears(10),
                LocalDate.now(),
                entries
        );

        return habitMapper.toDto(saved, entries, total);
    }

    // ------------------------------------------------------------
    //  Write: delete
    // ------------------------------------------------------------
    @Override
    @Transactional
    public void deleteHabit(Long id) {
        if (!habitRepository.existsById(id)) {
            throw new IllegalArgumentException("Habit not found: " + id);
        }
        habitRepository.deleteById(id);
    }

    // ------------------------------------------------------------
    //  Helpers
    // ------------------------------------------------------------

    /**
     * Number of days the habit has existed for, capped to the given window.
     * Rules:
     *   - Past days (before today) always count toward the total.
     *   - Today counts ONLY if today's entry is already marked completed.
     *     This way you're never "failed" for a day that isn't over yet,
     *     but if you HAVE completed today, it counts (so 1/1 = 100%).
     */
    private int computeTotalDays(Habit habit,
                                 LocalDate windowStart,
                                 LocalDate windowEnd,
                                 List<HabitEntry> entries) {
        if (habit.getCreatedDate() == null) return 0;

        LocalDate created = habit.getCreatedDate().toLocalDate();
        LocalDate today = LocalDate.now();

        LocalDate effStart = created.isAfter(windowStart) ? created : windowStart;
        LocalDate effEnd = today.isBefore(windowEnd) ? today : windowEnd;

        boolean todayCompleted = entries.stream()
                .anyMatch(e ->
                        e.getEntryDate() != null
                                && e.getEntryDate().equals(today)
                                && Boolean.TRUE.equals(e.getCompleted())
                );

        if (effEnd.equals(today) && !todayCompleted) {
            effEnd = effEnd.minusDays(1);
        }

        if (effEnd.isBefore(effStart)) return 0;
        return (int) ChronoUnit.DAYS.between(effStart, effEnd) + 1;
    }

    private String formatRange(LocalDate start, LocalDate end) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d");
        return start.format(fmt) + " - " + end.format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
    }
}