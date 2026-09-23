package com.railopt.ai.payload.forecasting;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateTrafficForecastRequest(
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
