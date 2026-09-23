package com.railopt.ai.payload.train;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record CreateTrainRouteRequest(
    @NotBlank
    @JsonProperty("route_code")
    String routeCode,

    @NotBlank
    @JsonProperty("name")
    String name,

    @NotBlank
    @JsonProperty("origin_station_id")
    String originStationId,

    @NotBlank
    @JsonProperty("destination_station_id")
    String destinationStationId
) {}
