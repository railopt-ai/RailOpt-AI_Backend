package com.railopt.ai.payload.train;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTrainRunRequest(
    @NotBlank
    @JsonProperty("train_route_id")
    String trainRouteId,

    @NotBlank
    @JsonProperty("train_schedule_id")
    String trainScheduleId,

    @NotBlank
    @JsonProperty("run_code")
    String runCode,

    @NotNull
    @JsonProperty("operation_date")
    LocalDate operationDate,

    @JsonProperty("planned_start_at")
    LocalDateTime plannedStartAt,

    @JsonProperty("planned_end_at")
    LocalDateTime plannedEndAt
) {}
