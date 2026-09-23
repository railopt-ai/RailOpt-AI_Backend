package com.railopt.ai.payload.scheduling;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateScheduledTaskRequest(
    @JsonProperty("status")
    String status,

    @JsonProperty("planned_start_at")
    LocalDateTime plannedStartAt,

    @JsonProperty("planned_end_at")
    LocalDateTime plannedEndAt,

    @JsonProperty("planned_duration_hours")
    Double plannedDurationHours,

    @JsonProperty("objective_contribution")
    Double objectiveContribution,

    @JsonProperty("scheduling_reason")
    String schedulingReason
) {}
