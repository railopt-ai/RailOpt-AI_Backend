package com.railopt.ai.payload.forecasting;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTrafficForecastRequest(
    @NotNull
    @JsonProperty("division_id")
    UUID divisionId,

    @NotBlank
    @JsonProperty("forecast_type")
    String forecastType,

    @NotNull
    @JsonProperty("forecast_date")
    LocalDate forecastDate,

    @NotNull
    @JsonProperty("horizon_start_at")
    LocalDateTime horizonStartAt,

    @NotNull
    @JsonProperty("horizon_end_at")
    LocalDateTime horizonEndAt,

    @JsonProperty("status")
    String status
) {}
