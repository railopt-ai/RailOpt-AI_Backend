package com.railopt.ai.payload.train;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateTrainRunRequest(
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
