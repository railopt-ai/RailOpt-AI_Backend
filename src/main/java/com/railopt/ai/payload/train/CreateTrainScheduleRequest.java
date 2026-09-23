package com.railopt.ai.payload.train;

import java.time.LocalDate;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTrainScheduleRequest(
    @NotBlank
    @JsonProperty("train_route_id")
    String trainRouteId,

    @NotBlank
    @JsonProperty("schedule_code")
    String scheduleCode,

    @NotNull
    @JsonProperty("valid_from")
    LocalDate validFrom,

    @NotNull
    @JsonProperty("valid_until")
    LocalDate validUntil,

    @NotBlank
    @JsonProperty("operating_days")
    String operatingDays,

    @NotNull
    @JsonProperty("departure_time")
    LocalTime departureTime,

    @NotNull
    @JsonProperty("arrival_time")
    LocalTime arrivalTime
) {}
