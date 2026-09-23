package com.railopt.ai.payload.train;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TrainRunResponse(
    @JsonProperty("id")
    UUID id,

    @JsonProperty("train_id")
    UUID trainId,

    @JsonProperty("train_route_id")
    UUID trainRouteId,

    @JsonProperty("train_schedule_id")
    UUID trainScheduleId,

    @JsonProperty("run_code")
    String runCode,

    @JsonProperty("operation_date")
    LocalDate operationDate,

    @JsonProperty("status")
    String status,

    @JsonProperty("planned_start_at")
    LocalDateTime plannedStartAt,

    @JsonProperty("planned_end_at")
    LocalDateTime plannedEndAt,

    @JsonProperty("actual_start_at")
    LocalDateTime actualStartAt,

    @JsonProperty("actual_end_at")
    LocalDateTime actualEndAt
) {}
