package com.railopt.ai.payload.train;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;

public record UpdateTrainRunStopRequest(
    @Min(1)
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
