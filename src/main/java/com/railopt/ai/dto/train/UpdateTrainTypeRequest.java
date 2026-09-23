package com.railopt.ai.dto.train;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateTrainTypeRequest(
    @JsonProperty("name")
    String name,

    @JsonProperty("description")
    String description,

    @JsonProperty("category")
    String category,

    @JsonProperty("priority_class")
    String priorityClass
) {}
