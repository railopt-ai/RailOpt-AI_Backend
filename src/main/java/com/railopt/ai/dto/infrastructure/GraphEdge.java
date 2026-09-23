package com.railopt.ai.dto.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GraphEdge(
    @JsonProperty("source")
    String source,

    @JsonProperty("target")
    String target,

    @JsonProperty("type")
    String type
) {}
