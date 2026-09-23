package com.railopt.ai.payload.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateBlockRequest(
    @JsonProperty("name")
    String name,

    @JsonProperty("code")
    String code
) {}
