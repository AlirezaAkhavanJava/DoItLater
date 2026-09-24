package com.arcade.doitlater.Repository;

import com.arcade.doitlater.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepository
        extends JpaRepository<Task, UUID> {


}
