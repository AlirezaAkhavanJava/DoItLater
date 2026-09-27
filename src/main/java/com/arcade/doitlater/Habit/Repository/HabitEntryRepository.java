package com.arcade.doitlater.Habit.Repository;

import com.arcade.doitlater.Habit.Domain.entity.HabitEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitEntryRepository extends JpaRepository<HabitEntry, Long> {

    List<HabitEntry> findByHabitId(Long habitId);

    Optional<HabitEntry> findByHabitIdAndEntryDate(Long habitId, LocalDate date);

    List<HabitEntry> findByHabitIdAndEntryDateBetween(Long habitId, LocalDate start, LocalDate end);

    @Query("""
                SELECT e FROM HabitEntry e
                WHERE e.habit.id IN :habitIds
                  AND e.entryDate BETWEEN :start AND :end
            """)
    List<HabitEntry> findByHabitIdsAndDateRange(
            @Param("habitIds") List<Long> habitIds,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);
}