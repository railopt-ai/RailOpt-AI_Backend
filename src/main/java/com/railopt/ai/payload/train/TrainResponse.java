package com.railopt.ai.payload.train;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TrainResponse(
    @JsonProperty("id")
    UUID id,

    @JsonProperty("train_type_id")
    UUID trainTypeId,

    @JsonProperty("train_number")
    String trainNumber,

    @JsonProperty("train_name")
    String trainName,

    @JsonProperty("operator")
    String operator,

    @JsonProperty("status")
    String status
) {}
