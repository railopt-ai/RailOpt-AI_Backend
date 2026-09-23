package com.railopt.ai.dto.scheduling;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record SchedulingRunTaskResponse(
    @JsonProperty("scheduling_run_id")
    UUID schedulingRunId,

    @JsonProperty("maintenance_task_id")
    UUID maintenanceTaskId,

    @JsonProperty("consideration_status")
    String considerationStatus,

    @JsonProperty("unscheduled_reason")
    String unscheduledReason,

    @JsonProperty("predicted_duration_hours")
    Double predictedDurationHours,

    @JsonProperty("effective_duration_hours")
    Double effectiveDurationHours,

    @JsonProperty("priority_score")
    Double priorityScore,

    @JsonProperty("risk_score")
    Double riskScore
) {}
