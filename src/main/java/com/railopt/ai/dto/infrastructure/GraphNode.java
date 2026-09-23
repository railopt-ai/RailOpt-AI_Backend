package com.railopt.ai.dto.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GraphNode(
    @JsonProperty("id")
    String id,

    @JsonProperty("type")
    String type,

    @JsonProperty("name")
    String name,

    @JsonProperty("status")
    String status
) {}
