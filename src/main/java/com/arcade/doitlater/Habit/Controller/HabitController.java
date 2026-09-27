package com.arcade.doitlater.Habit.Controller;

import com.arcade.doitlater.Habit.Domain.dto.CreateHabitRequestDto;
import com.arcade.doitlater.Habit.Domain.dto.ToggleHabitEntryRequestDto;
import com.arcade.doitlater.Habit.Domain.dto.request.CreateHabitRequest;
import com.arcade.doitlater.Habit.Domain.dto.request.ToggleHabitEntryRequest;
import com.arcade.doitlater.Habit.Domain.dto.response.HabitDto;
import com.arcade.doitlater.Habit.Domain.dto.response.HabitEntryDto;
import com.arcade.doitlater.Habit.Domain.dto.response.WeekGridDto;
import com.arcade.doitlater.Habit.Domain.entity.Habit;
import com.arcade.doitlater.Habit.Domain.entity.HabitEntry;
import com.arcade.doitlater.Habit.Service.HabitService;
import com.arcade.doitlater.Habit.mapper.HabitMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/habits")
public class HabitController {

    private final HabitMapper habitMapper;
    private final HabitService habitService;

    // ------------------------------------------------------------
    //  Create
    // ------------------------------------------------------------
    @PostMapping(path = "/create")
    public ResponseEntity<HabitDto> createHabit(
            @Valid @RequestBody CreateHabitRequestDto dto) {

        CreateHabitRequest request = habitMapper.fromDto(dto);
        Habit habit = habitService.createNewHabit(request);

        // Brand-new habit: no entries, totalDays = 0
        HabitDto response = habitMapper.toDto(habit, List.of(), 0);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ------------------------------------------------------------
    //  Read: list
    // ------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<HabitDto>> getAllHabits() {
        return ResponseEntity.ok(habitService.findAll());
    }

    // ------------------------------------------------------------
    //  Read: week grid
    // ------------------------------------------------------------
    @GetMapping("/week")
    public ResponseEntity<WeekGridDto> getWeekGrid(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {

        LocalDate start = (startDate != null)
                ? startDate
                : LocalDate.now().with(DayOfWeek.MONDAY);

        return ResponseEntity.ok(habitService.getWeekGrid(start));
    }

    // ------------------------------------------------------------
    //  Read: single habit (with full history — for detail page)
    // ------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<HabitDto> getHabit(@PathVariable Long id) {
        return ResponseEntity.ok(habitService.getHabit(id));
    }

    // ------------------------------------------------------------
    //  Write: toggle today's entry
    // ------------------------------------------------------------
    @PostMapping("/{habitId}/entries")
    public ResponseEntity<HabitEntryDto> toggleEntry(
            @PathVariable Long habitId,
            @Valid @RequestBody ToggleHabitEntryRequestDto dto) {

        ToggleHabitEntryRequest request = habitMapper.fromDto(dto);
        HabitEntry saved = habitService.toggleEntry(habitId, request);
        return ResponseEntity.ok(habitMapper.toDto(saved));
    }

    // ------------------------------------------------------------
    //  Write: update
    // ------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<HabitDto> updateHabit(
            @PathVariable Long id,
            @Valid @RequestBody CreateHabitRequestDto dto) {

        CreateHabitRequest request = habitMapper.fromDto(dto);
        return ResponseEntity.ok(habitService.updateHabit(id, request));
    }

    // ------------------------------------------------------------
    //  Write: delete
    // ------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
        return ResponseEntity.noContent().build();
    }
}