package com.arcade.doitlater.Habit.Repository;

import com.arcade.doitlater.Habit.Domain.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findAllByOrderByCreatedDateDesc();

}