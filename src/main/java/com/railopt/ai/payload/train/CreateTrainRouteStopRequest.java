package com.railopt.ai.payload.train;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTrainRouteStopRequest(
    @NotBlank
    @JsonProperty("station_id")
    String stationId,

    @NotNull
    @Min(1)
    @JsonProperty("sequence_no")
    Integer sequenceNo,

    @Min(0)
    @JsonProperty("arrival_offset_minutes")
    Integer arrivalOffsetMinutes,

    @Min(0)
    @JsonProperty("departure_offset_minutes")
    Integer departureOffsetMinutes
) {}
