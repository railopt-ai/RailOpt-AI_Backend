package com.railopt.ai.dto.train;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TrainRunStopResponse(
    @JsonProperty("id")
    UUID id,

    @JsonProperty("train_run_id")
    UUID trainRunId,

    @JsonProperty("station_id")
    UUID stationId,

    @JsonProperty("sequence_no")
    Integer sequenceNo,

    @JsonProperty("planned_arrival_at")
    LocalDateTime plannedArrivalAt,

    @JsonProperty("planned_departure_at")
    LocalDateTime plannedDepartureAt,

    @JsonProperty("actual_arrival_at")
    LocalDateTime actualArrivalAt,

    @JsonProperty("actual_departure_at")
    LocalDateTime actualDepartureAt
) {}
