package com.railopt.ai.payload.scheduling;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record SchedulingRunResponse(
    @JsonProperty("id")
    UUID id,

    @JsonProperty("created_by")
    UUID createdBy,

    @JsonProperty("run_code")
    String runCode,

    @JsonProperty("algorithm_type")
    String algorithmType,

    @JsonProperty("status")
    String status,

    @JsonProperty("planning_start_at")
    LocalDateTime planningStartAt,

    @JsonProperty("planning_end_at")
    LocalDateTime planningEndAt,

    @JsonProperty("started_at")
    LocalDateTime startedAt,

    @JsonProperty("completed_at")
    LocalDateTime completedAt,

    @JsonProperty("objective_description")
    String objectiveDescription,

    @JsonProperty("objective_weights")
    Map<String, Object> objectiveWeights,

    @JsonProperty("total_tasks_considered")
    Integer totalTasksConsidered,

    @JsonProperty("total_tasks_scheduled")
    Integer totalTasksScheduled,

    @JsonProperty("total_tasks_unscheduled")
    Integer totalTasksUnscheduled,

    @JsonProperty("error_message")
    String errorMessage
) {}
