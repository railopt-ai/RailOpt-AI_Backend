package com.railopt.ai.payload.organization;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DepartmentResponse(
    @JsonProperty("id")
    String id,

    @JsonProperty("name")
    String name,

    @JsonProperty("status")
    String status
) {}
