package com.railopt.ai.dto.forecasting;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TrafficForecastResponse(
    @JsonProperty("id")
    UUID id,

    @JsonProperty("division_id")
    UUID divisionId,

    @JsonProperty("forecast_type")
    String forecastType,

    @JsonProperty("forecast_date")
    LocalDate forecastDate,

    @JsonProperty("horizon_start_at")
    LocalDateTime horizonStartAt,

    @JsonProperty("horizon_end_at")
    LocalDateTime horizonEndAt,

    @JsonProperty("status")
    String status
) {}
