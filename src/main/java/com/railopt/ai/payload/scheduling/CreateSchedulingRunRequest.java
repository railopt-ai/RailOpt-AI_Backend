package com.railopt.ai.payload.scheduling;

import java.time.LocalDateTime;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSchedulingRunRequest(
    @NotBlank
    @JsonProperty("run_code")
    String runCode,

    @NotBlank
    @JsonProperty("algorithm_type")
    String algorithmType,

    @JsonProperty("status")
    String status,

    @NotNull
    @JsonProperty("planning_start_at")
    LocalDateTime planningStartAt,

    @NotNull
    @JsonProperty("planning_end_at")
    LocalDateTime planningEndAt,

    @JsonProperty("objective_description")
    String objectiveDescription,

    @JsonProperty("objective_weights")
    Map<String, Object> objectiveWeights
) {}
