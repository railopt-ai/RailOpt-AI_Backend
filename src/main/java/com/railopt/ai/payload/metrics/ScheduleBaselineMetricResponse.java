package com.railopt.ai.payload.metrics;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ScheduleBaselineMetricResponse(
    @JsonProperty("id")
    UUID id,

    @JsonProperty("scheduling_run_id")
    UUID schedulingRunId,

    @JsonProperty("total_tasks")
    Integer totalTasks,

    @JsonProperty("tasks_without_optimized_scheduling")
    Integer tasksWithoutOptimizedScheduling,

    @JsonProperty("estimated_block_hours")
    Integer estimatedBlockHours,

    @JsonProperty("estimated_train_delay_minutes")
    Double estimatedTrainDelayMinutes,

    @JsonProperty("estimated_asset_downtime_hours")
    Double estimatedAssetDowntimeHours,

    @JsonProperty("estimated_asset_availability_percentage")
    Double estimatedAssetAvailabilityPercentage,

    @JsonProperty("resource_utilization_percentage")
    Double resourceUtilizationPercentage,

    @JsonProperty("calculated_at")
    LocalDateTime calculatedAt
) {}
