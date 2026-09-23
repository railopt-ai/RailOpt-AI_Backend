package com.railopt.ai.payload.train;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TrainRouteResponse(
    @JsonProperty("id")
    UUID id,

    @JsonProperty("train_id")
    UUID trainId,

    @JsonProperty("route_code")
    String routeCode,

    @JsonProperty("name")
    String name,

    @JsonProperty("origin_station_id")
    UUID originStationId,

    @JsonProperty("destination_station_id")
    UUID destinationStationId,

    @JsonProperty("total_stations")
    Integer totalStations
) {}
