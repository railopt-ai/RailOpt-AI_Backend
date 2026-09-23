package com.railopt.ai.dto.organization;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DepartmentResponse(
    @JsonProperty("id")
    String id,

    @JsonProperty("name")
    String name,

    @JsonProperty("status")
    String status
) {}
