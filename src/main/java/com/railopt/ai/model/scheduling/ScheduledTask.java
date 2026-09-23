package com.railopt.ai.model.scheduling;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.railopt.ai.model.maintenance.MaintenanceTask;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/** Persistence mapping for table `scheduled_tasks`. */
@Entity
@Table(name = "scheduled_tasks")
@Getter
@Setter
@NoArgsConstructor
public class ScheduledTask {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scheduling_run_id", nullable = false, columnDefinition = "uuid")
    private SchedulingRun schedulingRun;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "maintenance_task_id", nullable = false, columnDefinition = "uuid")
    private MaintenanceTask maintenanceTask;

    @Column(name = "status", columnDefinition = "varchar")
    private String status;

    @Column(name = "planned_start_at", columnDefinition = "timestamp")
    private LocalDateTime plannedStartAt;

    @Column(name = "planned_end_at", columnDefinition = "timestamp")
    private LocalDateTime plannedEndAt;

    @Column(name = "planned_duration_hours", columnDefinition = "numeric")
    private BigDecimal plannedDurationHours;

    @Column(name = "objective_contribution", columnDefinition = "numeric")
    private BigDecimal objectiveContribution;

    @Column(name = "scheduling_reason", columnDefinition = "text")
    private String schedulingReason;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp")
    private LocalDateTime updatedAt;

}
