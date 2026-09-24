
package com.arcade.doitlater.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * JPA entity representing a task in the application's domain model.
 *
 * <p>This class is mapped to the {@code tasks} database table. Hibernate
 * uses the annotations on this class and its fields to translate between
 * Java objects and relational database rows.</p>
 *
 * <p>The entity contains both the task's business data (title, description,
 * status, priority, etc.) and its persistence metadata (identifier and
 * timestamps).</p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tasks")
public class Task {

    /**
     * Unique identifier of the task.
     *
     * <p>{@link GenerationType#UUID} tells the JPA provider to generate
     * a UUID automatically when a new Task is persisted.</p>
     *
     * <p>The identifier is marked as non-updatable because an entity's
     * primary key should remain stable throughout its lifecycle.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Short, human-readable name of the task.
     *
     * <p>The database column is non-nullable, meaning every persisted
     * task must have a title.</p>
     */
    @Column(nullable = false, name = "title")
    private String title;

    /**
     * Optional detailed information about the task.
     *
     * <p>The column is explicitly limited to 1050 characters. This provides
     * a persistence-level constraint on the expected size of descriptions.</p>
     */
    @Column(length = 1050, name = "description")
    private String description;

    /**
     * Calendar date on which the task is due.
     *
     * <p>{@link LocalDate} is appropriate here because a due date represents
     * a calendar date rather than a specific moment in time. It therefore
     * contains no timezone or time-of-day information.</p>
     */
    @Column(name = "due_date")
    private LocalDate dueDate;

    /**
     * Current lifecycle state of the task.
     *
     * <p>{@link EnumType#STRING} persists the enum constant's name rather
     * than its numeric ordinal. This is safer because changing the order
     * of enum constants will not change the meaning of existing database
     * records.</p>
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    /**
     * Priority assigned to the task.
     *
     * <p>The enum is persisted using its name rather than its ordinal.
     * This keeps the database representation stable even if the Java enum
     * declaration is reordered later.</p>
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private TaskPriority priority;

    /**
     * Timestamp indicating when the task was originally created.
     *
     * <p>{@link Instant} represents an absolute point in time and is
     * therefore suitable for persistence-level timestamps independent
     * of the application's local timezone.</p>
     *
     * <p>{@code updatable = false} prevents Hibernate from modifying this
     * column when the entity is updated. The creation timestamp should
     * represent the original creation event permanently.</p>
     */
    @Column(name = "created", nullable = false, updatable = false)
    private Instant created;

    /**
     * Timestamp indicating when the task was most recently modified.
     *
     * <p>Unlike {@code created}, this value is expected to change whenever
     * the persistent state of the task is modified.</p>
     */
    @Column(name = "updated", nullable = false)
    private Instant updated;

    /**
     * Determines whether this Task represents the same persistent entity
     * as another Task instance.
     *
     * <p>Entity equality is based on the primary key rather than mutable
     * business fields such as title, description, or status. Two Task
     * instances with the same identifier represent the same database row.</p>
     *
     * <p>{@link Objects#equals(Object, Object)} safely handles {@code null}
     * identifiers, which can occur while an entity is still transient and
     * has not yet been persisted.</p>
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    /**
     * Generates the hash code corresponding to the entity's identity.
     *
     * <p>The implementation uses the same field used by {@link #equals(Object)}
     * to preserve Java's equals/hashCode contract. This is important when
     * Task instances are stored in hash-based collections such as HashSet
     * or used as keys in HashMap.</p>
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /**
     * Returns a human-readable representation of this Task.
     *
     * <p>This is primarily useful for debugging and logging. Be careful
     * when adding sensitive information to this method because its output
     * may be written to application logs.</p>
     */
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", status=" + status +
                ", priority=" + priority +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}

