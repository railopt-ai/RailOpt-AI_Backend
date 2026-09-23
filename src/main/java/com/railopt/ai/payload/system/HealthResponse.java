package com.railopt.ai.payload.system;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HealthResponse(
    @JsonProperty("status")
    String status
) {}
