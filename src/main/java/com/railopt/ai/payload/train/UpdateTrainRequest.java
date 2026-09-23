package com.railopt.ai.payload.train;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateTrainRequest(
    @JsonProperty("train_type_id")
    String trainTypeId,

    @JsonProperty("train_number")
    String trainNumber,

    @JsonProperty("train_name")
    String trainName,

    @JsonProperty("operator")
    String operator
) {}
