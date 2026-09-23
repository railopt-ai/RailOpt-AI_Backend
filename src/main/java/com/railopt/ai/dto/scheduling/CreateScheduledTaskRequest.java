package com.railopt.ai.dto.scheduling;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record CreateScheduledTaskRequest(
    @NotNull
    @JsonProperty("maintenance_task_id")
    UUID maintenanceTaskId,

    @JsonProperty("status")
    String status,

    @NotNull
    @JsonProperty("planned_start_at")
    LocalDateTime plannedStartAt,

    @NotNull
    @JsonProperty("planned_end_at")
    LocalDateTime plannedEndAt,

    @JsonProperty("planned_duration_hours")
    Double plannedDurationHours,

    @JsonProperty("objective_contribution")
    Double objectiveContribution,

    @JsonProperty("scheduling_reason")
    String schedulingReason
) {}
