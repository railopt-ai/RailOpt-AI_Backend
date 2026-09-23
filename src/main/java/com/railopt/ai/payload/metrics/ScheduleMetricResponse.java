package com.railopt.ai.payload.metrics;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ScheduleMetricResponse(
    @JsonProperty("id")
    UUID id,

    @JsonProperty("scheduling_run_id")
    UUID schedulingRunId,

    @JsonProperty("total_tasks")
    Integer totalTasks,

    @JsonProperty("scheduled_tasks")
    Integer scheduledTasks,

    @JsonProperty("unscheduled_tasks")
    Integer unscheduledTasks,

    @JsonProperty("total_blocks")
    Integer totalBlocks,

    @JsonProperty("total_block_hours")
    Integer totalBlockHours,

    @JsonProperty("train_conflicts")
    Integer trainConflicts,

    @JsonProperty("resource_conflicts")
    Integer resourceConflicts,

    @JsonProperty("safety_conflicts")
    Integer safetyConflicts,

    @JsonProperty("estimated_train_delay_minutes")
    Double estimatedTrainDelayMinutes,

    @JsonProperty("estimated_asset_downtime_hours")
    Double estimatedAssetDowntimeHours,

    @JsonProperty("estimated_asset_availability_percentage")
    Double estimatedAssetAvailabilityPercentage,

    @JsonProperty("resource_utilization_percentage")
    Double resourceUtilizationPercentage,

    @JsonProperty("maintenance_completion_percentage")
    Double maintenanceCompletionPercentage,

    @JsonProperty("total_objective_score")
    Double totalObjectiveScore,

    @JsonProperty("calculated_at")
    LocalDateTime calculatedAt
) {}
