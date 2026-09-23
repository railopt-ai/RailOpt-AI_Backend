package com.railopt.ai.payload.forecasting;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;

public record UpdateTrafficForecastItemRequest(
    @JsonProperty("block_section_id")
    UUID blockSectionId,

    @JsonProperty("time_window_start")
    LocalDateTime timeWindowStart,

    @JsonProperty("time_window_end")
    LocalDateTime timeWindowEnd,

    @JsonProperty("train_category")
    String trainCategory,

    @Min(0)
    @JsonProperty("expected_train_count")
    Integer expectedTrainCount,

    @JsonProperty("probability")
    Double probability,

    @JsonProperty("expected_passenger_trains")
    Integer expectedPassengerTrains,

    @JsonProperty("expected_goods_trains")
    Integer expectedGoodsTrains
) {}
