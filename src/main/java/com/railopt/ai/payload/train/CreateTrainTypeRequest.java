package com.railopt.ai.payload.train;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record CreateTrainTypeRequest(
    @NotBlank
    @JsonProperty("code")
    String code,

    @NotBlank
    @JsonProperty("name")
    String name,

    @JsonProperty("description")
    String description,

    @NotBlank
    @JsonProperty("category")
    String category,

    @NotBlank
    @JsonProperty("priority_class")
    String priorityClass
) {}
