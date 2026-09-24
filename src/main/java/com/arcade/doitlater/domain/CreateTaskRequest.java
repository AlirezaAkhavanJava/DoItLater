
package com.arcade.doitlater.domain;

import com.arcade.doitlater.domain.entity.TaskPriority;

import java.time.LocalDate;

/**
 * Request DTO used when creating a new Task.
 *
 * <p>This record represents the data that the application expects from a
 * client when a task is created. It intentionally contains only the fields
 * that the client is allowed or expected to provide during task creation.</p>
 *
 * <p>This type is a DTO (Data Transfer Object), not a JPA entity. It exists
 * to define a clear boundary between the external request and the internal
 * {@link com.arcade.doitlater.domain.entity.Task} persistence model.</p>
 *
 * <p>Keeping the request separate from the entity prevents API input from
 * being tightly coupled to the database model. The Task entity may contain
 * fields such as {@code id}, {@code status}, {@code created}, and
 * {@code updated} that should not be supplied directly by a client when
 * creating a task.</p>
 *
 * <p>As a Java {@code record}, this class is immutable and automatically
 * provides a constructor, accessors, {@code equals()}, {@code hashCode()},
 * and {@code toString()}.</p>
 */
public record CreateTaskRequest(
        String title,
        String description,
        LocalDate dueDate,
        TaskPriority priority
) {
}

