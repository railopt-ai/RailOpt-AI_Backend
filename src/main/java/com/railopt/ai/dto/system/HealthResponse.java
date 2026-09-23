package com.railopt.ai.dto.system;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HealthResponse(
    @JsonProperty("status")
    String status
) {}
