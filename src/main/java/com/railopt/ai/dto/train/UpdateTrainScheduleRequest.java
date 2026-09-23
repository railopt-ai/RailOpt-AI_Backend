package com.railopt.ai.dto.train;

import java.time.LocalDate;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateTrainScheduleRequest(
    @JsonProperty("train_route_id")
    String trainRouteId,

    @JsonProperty("valid_from")
    LocalDate validFrom,

    @JsonProperty("valid_until")
    LocalDate validUntil,

    @JsonProperty("operating_days")
    String operatingDays,

    @JsonProperty("departure_time")
    LocalTime departureTime,

    @JsonProperty("arrival_time")
    LocalTime arrivalTime,

    @JsonProperty("status")
    String status
) {}
