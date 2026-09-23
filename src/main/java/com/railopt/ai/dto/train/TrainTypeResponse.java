package com.railopt.ai.dto.train;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TrainTypeResponse(
    @JsonProperty("id")
    UUID id,

    @JsonProperty("code")
    String code,

    @JsonProperty("name")
    String name,

    @JsonProperty("description")
    String description,

    @JsonProperty("category")
    String category,

    @JsonProperty("priority_class")
    String priorityClass
) {}
