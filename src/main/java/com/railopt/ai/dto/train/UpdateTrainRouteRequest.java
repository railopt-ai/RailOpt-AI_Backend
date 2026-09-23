package com.railopt.ai.dto.train;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateTrainRouteRequest(
    @JsonProperty("route_code")
    String routeCode,

    @JsonProperty("name")
    String name,

    @JsonProperty("origin_station_id")
    String originStationId,

    @JsonProperty("destination_station_id")
    String destinationStationId
) {}
