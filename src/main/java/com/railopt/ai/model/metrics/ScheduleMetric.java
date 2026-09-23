package com.railopt.ai.model.metrics;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.railopt.ai.model.scheduling.SchedulingRun;

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

/** Persistence mapping for table `schedule_metrics`. */
@Entity
@Table(name = "schedule_metrics")
@Getter
@Setter
@NoArgsConstructor
public class ScheduleMetric {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scheduling_run_id", nullable = false, columnDefinition = "uuid")
    private SchedulingRun schedulingRun;

    @Column(name = "total_tasks", columnDefinition = "integer")
    private Integer totalTasks;

    @Column(name = "scheduled_tasks", columnDefinition = "integer")
    private Integer scheduledTasks;

    @Column(name = "unscheduled_tasks", columnDefinition = "integer")
    private Integer unscheduledTasks;

    @Column(name = "total_blocks", columnDefinition = "integer")
    private Integer totalBlocks;

    @Column(name = "total_block_hours", columnDefinition = "integer")
    private Integer totalBlockHours;

    @Column(name = "train_conflicts", columnDefinition = "integer")
    private Integer trainConflicts;

    @Column(name = "resource_conflicts", columnDefinition = "integer")
    private Integer resourceConflicts;

    @Column(name = "safety_conflicts", columnDefinition = "integer")
    private Integer safetyConflicts;

    @Column(name = "estimated_train_delay_minutes", columnDefinition = "numeric")
    private BigDecimal estimatedTrainDelayMinutes;

    @Column(name = "estimated_asset_downtime_hours", columnDefinition = "numeric")
    private BigDecimal estimatedAssetDowntimeHours;

    @Column(name = "estimated_asset_availability_percentage", columnDefinition = "numeric")
    private BigDecimal estimatedAssetAvailabilityPercentage;

    @Column(name = "resource_utilization_percentage", columnDefinition = "numeric")
    private BigDecimal resourceUtilizationPercentage;

    @Column(name = "maintenance_completion_percentage", columnDefinition = "numeric")
    private BigDecimal maintenanceCompletionPercentage;

    @Column(name = "total_objective_score", columnDefinition = "numeric")
    private BigDecimal totalObjectiveScore;

    @Column(name = "calculated_at", columnDefinition = "timestamp")
    private LocalDateTime calculatedAt;

}
