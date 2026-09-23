package com.railopt.ai.model.scheduling;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.railopt.ai.model.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/** Persistence mapping for table `scheduling_runs`. */
@Entity
@Table(
        name = "scheduling_runs",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "run_code")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class SchedulingRun {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false, columnDefinition = "uuid")
    private User createdBy;

    @Column(name = "run_code", nullable = false, unique = true, columnDefinition = "varchar")
    private String runCode;

    @Column(name = "algorithm_type", columnDefinition = "varchar")
    private String algorithmType;

    @Column(name = "status", columnDefinition = "varchar")
    private String status;

    @Column(name = "planning_start_at", columnDefinition = "timestamp")
    private LocalDateTime planningStartAt;

    @Column(name = "planning_end_at", columnDefinition = "timestamp")
    private LocalDateTime planningEndAt;

    @Column(name = "started_at", columnDefinition = "timestamp")
    private LocalDateTime startedAt;

    @Column(name = "completed_at", columnDefinition = "timestamp")
    private LocalDateTime completedAt;

    @Column(name = "objective_description", columnDefinition = "text")
    private String objectiveDescription;

    /** PostgreSQL JSONB; Hibernate JSON JDBC type. */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "objective_weights", columnDefinition = "jsonb")
    private Map<String, Object> objectiveWeights;

    @Column(name = "total_tasks_considered", columnDefinition = "integer")
    private Integer totalTasksConsidered;

    @Column(name = "total_tasks_scheduled", columnDefinition = "integer")
    private Integer totalTasksScheduled;

    @Column(name = "total_tasks_unscheduled", columnDefinition = "integer")
    private Integer totalTasksUnscheduled;

    @Column(name = "error_message", columnDefinition = "text")
    private String errorMessage;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp")
    private LocalDateTime updatedAt;

}
