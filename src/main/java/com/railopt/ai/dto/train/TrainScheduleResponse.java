package com.railopt.ai.dto.train;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TrainScheduleResponse(
    @JsonProperty("id")
    UUID id,

    @JsonProperty("train_id")
    UUID trainId,

    @JsonProperty("train_route_id")
    UUID trainRouteId,

    @JsonProperty("schedule_code")
    String scheduleCode,

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
