package com.railopt.ai.payload.train;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTrainRunStopRequest(
    @NotBlank
    @JsonProperty("station_id")
    String stationId,

    @NotNull
    @Min(1)
    @JsonProperty("sequence_no")
    Integer sequenceNo,

    @JsonProperty("planned_arrival_at")
    LocalDateTime plannedArrivalAt,

    @JsonProperty("planned_departure_at")
    LocalDateTime plannedDepartureAt
) {}
