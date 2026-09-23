package com.railopt.ai.payload.train;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TrainRouteStopResponse(
    @JsonProperty("id")
    UUID id,

    @JsonProperty("route_id")
    UUID routeId,

    @JsonProperty("station_id")
    UUID stationId,

    @JsonProperty("sequence_no")
    Integer sequenceNo,

    @JsonProperty("arrival_offset_minutes")
    Integer arrivalOffsetMinutes,

    @JsonProperty("departure_offset_minutes")
    Integer departureOffsetMinutes
) {}
