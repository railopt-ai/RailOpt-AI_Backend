package com.railopt.ai.payload.train;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record CreateTrainRequest(
    @NotBlank
    @JsonProperty("train_type_id")
    String trainTypeId,

    @NotBlank
    @JsonProperty("train_number")
    String trainNumber,

    @JsonProperty("train_name")
    String trainName,

    @JsonProperty("operator")
    String operator
) {}
